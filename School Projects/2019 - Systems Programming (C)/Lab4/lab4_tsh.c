/* 
 * tsh - A tiny shell program with job control
 * 
 * Vitaliy Shydlonok : vshydlon
 * and
 * Shaun Godfrey : sgodfre3
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>

/* Misc manifest constants */
#define MAXLINE    1024   /* max line size */
#define MAXARGS     128   /* max args on a command line */
#define MAXJOBS      16   /* max jobs at any point in time */
#define MAXJID    1<<16   /* max job ID */

/* Job states */
#define UNDEF 0 /* undefined */
#define FG 1    /* running in foreground */
#define BG 2    /* running in background */
#define ST 3    /* stopped */

/* 
 * Jobs states: FG (foreground), BG (background), ST (stopped)
 * Job state transitions and enabling actions:
 *     FG -> ST  : ctrl-z
 *     ST -> FG  : fg command
 *     ST -> BG  : bg command
 *     BG -> FG  : fg command
 * At most 1 job can be in the FG state.
 */

/* Global variables */
extern char **environ;      /* defined in libc */
char prompt[] = "tsh> ";    /* command line prompt (DO NOT CHANGE) */
int verbose = 0;            /* if true, print additional output */
int nextjid = 1;            /* next job ID to allocate */
char sbuf[MAXLINE];         /* for composing sprintf messages */

struct job_t {              /* The job struct */
    pid_t pid;              /* job PID */
    int jid;                /* job ID [1, 2, ...] */
    int state;              /* UNDEF, BG, FG, or ST */
    char cmdline[MAXLINE];  /* command line */
};
struct job_t jobs[MAXJOBS]; /* The job list */

/* End global variables */


/* Function prototypes */

/* Here are the functions that you will implement */
void eval(char *cmdline);
int builtin_cmd(char **argv);
void do_bgfg(char **argv);
void waitfg(pid_t pid);

void sigchld_handler(int sig);
void sigtstp_handler(int sig);
void sigint_handler(int sig);

/* Here are helper routines that we've provided for you */
int parseline(const char *cmdline, char **argv); 
void sigquit_handler(int sig);

void clearjob(struct job_t *job);
void initjobs(struct job_t *jobs);
int maxjid(struct job_t *jobs); 
int addjob(struct job_t *jobs, pid_t pid, int state, char *cmdline);
int deletejob(struct job_t *jobs, pid_t pid); 
pid_t fgpid(struct job_t *jobs);
struct job_t *getjobpid(struct job_t *jobs, pid_t pid);
struct job_t *getjobjid(struct job_t *jobs, int jid); 
int pid2jid(pid_t pid); 
void listjobs(struct job_t *jobs);

/* Wrapper functions from csapp */
typedef void handler_t(int);
handler_t *Signal(int signum, handler_t *handler);
void Sigprocmask(int how, const sigset_t *set, sigset_t *oldset);
void Sigemptyset(sigset_t *set);
void Sigfillset(sigset_t *set);
void Sigaddset(sigset_t *set, int signum);
void Sigdelset(sigset_t *set, int signum);
int Sigsuspend(const sigset_t *set);
void Kill(pid_t pid, int signum);
void Setpgid(pid_t pid, pid_t pgid);

/* Safe io from csapp */
ssize_t sio_puts(char s[]);
ssize_t sio_putl(long v);
void sio_error(char s[]);
ssize_t Sio_puts(char s[]);
ssize_t Sio_putl(long v);
void Sio_error(char s[]);

/* Other helper routines */
void usage(void);
void unix_error(char *msg);
void app_error(char *msg);
typedef void handler_t(int);

/*
 * main - The shell's main routine 
 */
int main(int argc, char **argv) 
{
    char c;
    char cmdline[MAXLINE];
    int emit_prompt = 1; /* emit prompt (default) */

    /* Redirect stderr to stdout (so that driver will get all output
     * on the pipe connected to stdout) */
    dup2(1, 2);

    /* Parse the command line */
    while ((c = getopt(argc, argv, "hvp")) != EOF) {
        switch (c) {
        case 'h':             /* print help message */
            usage();
	    break;
        case 'v':             /* emit additional diagnostic info */
            verbose = 1;
	    break;
        case 'p':             /* don't print a prompt */
            emit_prompt = 0;  /* handy for automatic testing */
	    break;
	default:
            usage();
	}
    }

    /* Install the signal handlers */

    /* These are the ones you will need to implement */
    Signal(SIGINT,  sigint_handler);   /* ctrl-c */
    Signal(SIGTSTP, sigtstp_handler);  /* ctrl-z */
    Signal(SIGCHLD, sigchld_handler);  /* Terminated or stopped child */

    /* This one provides a clean way to kill the shell */
    Signal(SIGQUIT, sigquit_handler); 

    /* Initialize the job list */
    initjobs(jobs);

    /* Execute the shell's read/eval loop */
    while (1) {

	/* Read command line */
	if (emit_prompt) {
	    printf("%s", prompt);
	    fflush(stdout);
	}
	if ((fgets(cmdline, MAXLINE, stdin) == NULL) && ferror(stdin))
	    app_error("fgets error");
	if (feof(stdin)) { /* End of file (ctrl-d) */
	    fflush(stdout);
	    exit(0);
	}

	/* Evaluate the command line */
	eval(cmdline);
	fflush(stdout);
	fflush(stdout);
    } 

    exit(0); /* control never reaches here */
}
  
/* 
 * eval - Evaluate the command line that the user has just typed in
 * 
 * If the user has requested a built-in command (quit, jobs, bg or fg)
 * then execute it immediately. Otherwise, fork a child process and
 * run the job in the context of the child. If the job is running in
 * the foreground, wait for it to terminate and then return.  Note:
 * each child process must have a unique process group ID so that our
 * background children don't receive SIGINT (SIGTSTP) from the kernel
 * when we type ctrl-c (ctrl-z) at the keyboard.  
*/
void eval(char *cmdline) 
{
	int olderrno = errno;
	char *argv[MAXARGS];
	int bg;
	int state;
	pid_t pid;
	
	// Signal masks
	sigset_t mask_all, mask_one, prev_one;
	Sigfillset(&mask_all);
	Sigemptyset(&mask_one);
	Sigaddset(&mask_one, SIGCHLD);
	
	// Parse the command
	bg = parseline(cmdline, argv);
	
	// Ignore empty lines
	if (argv[0] == NULL)
		return;
	
	// Process built in functions
	if (!builtin_cmd(argv)) {
		
		// Block the SIGCHLD signal
		Sigprocmask(SIG_BLOCK, &mask_one, &prev_one);
		
		// Make a child to run the job
		if ((pid = fork()) == 0) {
			
			// Unblock the SIGCHLD signal for the child
			Sigprocmask(SIG_SETMASK, &prev_one, NULL);
			
			// Put the child in a new process group
			Setpgid(0, 0);
			
			// Make sure that the job is executed
			if (execve(argv[0], argv, environ) < 0) {
				
				Sio_puts(argv[0]);
				Sio_puts(": Command not found.\n");
			}
		
			exit(0);
		}
		
		// Determine the state of the new job
		state = (bg) ? BG : FG;
		
		// Block all signals until the job is added to the job list
		Sigprocmask(SIG_BLOCK, &mask_all, NULL); 
		
		// Add the new job to the job list
		addjob(jobs, pid, state, cmdline);
		
		// Unblock SIGCHLD after the job is added
		Sigprocmask(SIG_SETMASK, &prev_one, NULL); 
		
		// Wait for the child to terminate if it's a foreground job
		if (!bg) {
			
			waitfg(pid);
		}
		else {
			
			// Print the new background job information
			Sio_puts("[");
			Sio_putl((long)pid2jid(pid));
			Sio_puts("] (");
			Sio_putl((long)pid);
			Sio_puts(") ");
			Sio_puts(cmdline);
		}
	}
	
	errno = olderrno;
    return;
}

/* 
 * parseline - Parse the command line and build the argv array.
 * 
 * Characters enclosed in single quotes are treated as a single
 * argument.  Return true if the user has requested a BG job, false if
 * the user has requested a FG job.  
 */
int parseline(const char *cmdline, char **argv) 
{
    static char array[MAXLINE]; /* holds local copy of command line */
    char *buf = array;          /* ptr that traverses command line */
    char *delim;                /* points to first space delimiter */
    int argc;                   /* number of args */
    int bg;                     /* background job? */

    strcpy(buf, cmdline);
    buf[strlen(buf)-1] = ' ';  /* replace trailing '\n' with space */
    while (*buf && (*buf == ' ')) /* ignore leading spaces */
	buf++;

    /* Build the argv list */
    argc = 0;
    if (*buf == '\'') {
	buf++;
	delim = strchr(buf, '\'');
    }
    else {
	delim = strchr(buf, ' ');
    }

    while (delim) {
	argv[argc++] = buf;
	*delim = '\0';
	buf = delim + 1;
	while (*buf && (*buf == ' ')) /* ignore spaces */
	       buf++;

	if (*buf == '\'') {
	    buf++;
	    delim = strchr(buf, '\'');
	}
	else {
	    delim = strchr(buf, ' ');
	}
    }
    argv[argc] = NULL;
    
    if (argc == 0)  /* ignore blank line */
	return 1;

    /* should the job run in the background? */
    if ((bg = (*argv[argc-1] == '&')) != 0) {
	argv[--argc] = NULL;
    }
    return bg;
}

/* 
 * builtin_cmd - If the user has typed a built-in command then execute
 *    it immediately.  
 */
int builtin_cmd(char **argv) 
{
	// Determine whether the command is a built-in command
	if (strcmp(argv[0], "quit") == 0) {
		
		// Exit the program
		exit(0);
		return 1;
	}
	else if (strcmp(argv[0], "jobs") == 0) {
		
		// List the currently running jobs
		listjobs(jobs);
		return 1;
	}
	else if (strcmp(argv[0], "bg") == 0 || strcmp(argv[0], "fg") == 0) {
		
		// Run the bg or fg command
		do_bgfg(argv);
		return 1;
	}

    return 0;     /* not a builtin command */
}

/* 
 * do_bgfg - Execute the builtin bg and fg commands
 */
void do_bgfg(char **argv) 
{
	int olderrno = errno;
	
	pid_t pid = 0;
	int jid = 0;
	struct job_t *job;
	
	// Make sure that the command has a PID or JID
	if (argv[1] == NULL) {
		Sio_puts(argv[0]);
		Sio_puts(" command requires PID or %jobid argument\n");
	}
	else {
		
		// Check if the parameter is a jid
		if (argv[1][0] == '%') {
		
			// Extract the jid string from the parameter
			char jidStr[16];
			strcpy(jidStr, argv[1]+sizeof(char));
			
			// Convert the jid string into an int and get the job associated with it
			jid = atoi(jidStr);
			job = getjobjid(jobs, jid);
		}
		else {
			
			// Convert the parameter into a pid and get the job associated with it
			pid = atoi(argv[1]);
			job = getjobpid(jobs, pid);
		}
		
		// Make sure that a valid pid or jid was entered
		if (jid <= 0 && pid <= 0) {
			
			Sio_puts(argv[0]);
			Sio_puts(": argument must be a PID or %jobid\n");
		} 
		else if (job == NULL) { // Make sure that the job exists
			
			// Print an error based on whether it was a jid or pid
			if (argv[1][0] == '%') {
				
				Sio_puts(argv[1]);
				Sio_puts(": No such job\n");
			}
			else {
				
				Sio_puts("(");
				Sio_puts(argv[1]);
				Sio_puts("): No such process\n");
			}
		}
		else {
			
			// Determine which command, bg or fg, was used
			if (strcmp(argv[0], "bg") == 0) {
			
				// Print the job information
				Sio_puts("[");
				Sio_putl((long)job->jid);
				Sio_puts("] (");
				Sio_putl((long)job->pid);
				Sio_puts(") ");
				Sio_puts(job->cmdline);
			
				// Send SIGCONT to the process group
				Kill(- job->pid, SIGCONT);
				
				// Send the job to the background
				job->state = BG;
			}
			else {
				
				// Get the foreground pid
				pid_t fgproc = fgpid(jobs);
				
				// Check if a foreground job exists
				if (fgproc) {
					
					// Send the foreground job to the background
					struct job_t *fgjob;
					fgjob = getjobpid(jobs, fgproc);
					fgjob->state = BG;
				}
				
				// Send the job a SIGCONT signal
				Kill(- job->pid, SIGCONT);
				
				// Send the job to the foreground
				job->state = FG;
				
				// Wait for the new foreground job to finish
				waitfg(job->pid);
			}
		}
	}
	
	errno = olderrno;
    return;
}

/* 
 * waitfg - Block until process pid is no longer the foreground process
 */
void waitfg(pid_t pid)
{
	// Signal masks
	sigset_t mask, prev;
	Sigemptyset(&mask);
	Sigemptyset(&prev);
    Sigaddset(&mask, SIGCHLD);
    
    // Unblock everything
    Sigprocmask(SIG_BLOCK, &mask, &prev);
    
    // Sleep while the foreground process runs
    while (fgpid(jobs) == pid)
        Sigsuspend(&prev);
    
    // Revert the signal mask
    Sigprocmask(SIG_SETMASK, &prev, NULL);
	
    return;
}

/*****************
 * Signal handlers
 *****************/

/* 
 * sigchld_handler - The kernel sends a SIGCHLD to the shell whenever
 *     a child job terminates (becomes a zombie), or stops because it
 *     received a SIGSTOP or SIGTSTP signal. The handler reaps all
 *     available zombie children, but doesn't wait for any other
 *     currently running children to terminate.  
 */
void sigchld_handler(int sig) 
{
	int olderrno = errno;
	int status;
	pid_t pid;
	
	// Signal masks
	sigset_t mask_all, prev_all;
	Sigfillset(&mask_all);
	
	// Reap every terminated child
	while ((pid = waitpid(-1, &status, WNOHANG | WUNTRACED)) > 0) {
		
		// Block all signals until the job is deleted from the job list
		Sigprocmask(SIG_BLOCK, &mask_all, &prev_all);
		
		// Get the job that recieved the signal
		struct job_t *job = getjobpid(jobs, pid);
		
		// Determine what signal was caught
		if(WIFEXITED(status)) {
			
			// Delete the exited job from the job list
			deletejob(jobs, pid);
		}
		else if (WIFSIGNALED(status)) {
			
			// Check if the job has been "handled" or if the signal came from the outside
			if (job->state != UNDEF) {
				
				// Print the job information and a terminated message
				Sio_puts("Job [");
				Sio_putl((long)pid2jid(pid));
				Sio_puts("] (");
				Sio_putl((long)pid);
				Sio_puts(") terminated by signal ");
				Sio_putl((long)SIGINT);
				Sio_puts("\n");
			}
			
			// Delete the terminated job from the job list
			deletejob(jobs, pid);
		}
		else if (WIFSTOPPED(status)) {
			
			// Check if the job has been "handled" or if the signal came from the outside
			if (job->state != UNDEF) {
				
				// Print the job information and a stopped message
				Sio_puts("Job [");
				Sio_putl((long)pid2jid(pid));
				Sio_puts("] (");
				Sio_putl((long)pid);
				Sio_puts(") stopped by signal ");
				Sio_putl((long)SIGTSTP);
				Sio_puts("\n");
			}
			
			// Change the job's status to stopped
			job->state = ST;
		}
		
		// Revert the signals mask
		Sigprocmask(SIG_SETMASK, &prev_all, NULL);
	}
	
	// Check for a wait error, excluding the "no child process" error
	if (pid < 0 && errno != ECHILD)
		unix_error("Wait error");
		
	errno = olderrno;	
    return;
}

/* 
 * sigint_handler - The kernel sends a SIGINT to the shell whenver the
 *    user types ctrl-c at the keyboard.  Catch it and send it along
 *    to the foreground job.  
 */
void sigint_handler(int sig) 
{
	int olderrno = errno;
	pid_t pid;
	
	// Signal masks
	sigset_t mask_all, prev_all;
	Sigfillset(&mask_all);
	
	// Block all signals until the kill command is executed
	Sigprocmask(SIG_BLOCK, &mask_all, &prev_all);
	
	// Get the foreground job's pid and make sure it exists
	pid = fgpid(jobs);
	if (!pid)
		return;
	
	// Print the job information and terminated message
	Sio_puts("Job [");
	Sio_putl((long)pid2jid(pid));
	Sio_puts("] (");
	Sio_putl((long)pid);
	Sio_puts(") terminated by signal ");
	Sio_putl((long)sig);
	Sio_puts("\n");
	
	// Get the job being terminated
	struct job_t *job = getjobpid(jobs, pid);
	
	// Flag the job as "handled", by setting it's state to UNDEF
	job->state = UNDEF;
	
	// Send the SIGINT signal to the foreground job
	Kill(-pid, sig);
	
	// Revert the signals mask
	Sigprocmask(SIG_SETMASK, &prev_all, NULL);
	
	errno = olderrno;
    return;
}

/*
 * sigtstp_handler - The kernel sends a SIGTSTP to the shell whenever
 *     the user types ctrl-z at the keyboard. Catch it and suspend the
 *     foreground job by sending it a SIGTSTP.  
 */
void sigtstp_handler(int sig) 
{
	int olderrno = errno;
	pid_t pid;
	
	// Signal masks
	sigset_t mask_all, prev_all;
	Sigfillset(&mask_all);
	
	// Block all signals until the kill command is executed
	Sigprocmask(SIG_BLOCK, &mask_all, &prev_all);
	
	// Get the foreground job's pid and make sure it exists
	pid = fgpid(jobs);
	if (!pid)
		return;
	
	// Print the job information and stopped message
	Sio_puts("Job [");
	Sio_putl((long)pid2jid(pid));
	Sio_puts("] (");
	Sio_putl((long)pid);
	Sio_puts(") stopped by signal ");
	Sio_putl((long)sig);
	Sio_puts("\n");
	
	// Get the job being stopped
	struct job_t *job = getjobpid(jobs, pid);
	
	// Flag the job as "handled", by setting it's state to UNDEF
	job->state = UNDEF;
	
	// Send the SIGTSTP signal to the foreground job
	Kill(-pid, sig);
	
	// Revert the signals mask
	Sigprocmask(SIG_SETMASK, &prev_all, NULL);
	
	errno = olderrno;
    return;
}

/*********************
 * End signal handlers
 *********************/

/***********************************************
 * Helper routines that manipulate the job list
 **********************************************/

/* clearjob - Clear the entries in a job struct */
void clearjob(struct job_t *job) {
    job->pid = 0;
    job->jid = 0;
    job->state = UNDEF;
    job->cmdline[0] = '\0';
}

/* initjobs - Initialize the job list */
void initjobs(struct job_t *jobs) {
    int i;

    for (i = 0; i < MAXJOBS; i++)
	clearjob(&jobs[i]);
}

/* maxjid - Returns largest allocated job ID */
int maxjid(struct job_t *jobs) 
{
    int i, max=0;

    for (i = 0; i < MAXJOBS; i++)
	if (jobs[i].jid > max)
	    max = jobs[i].jid;
    return max;
}

/* addjob - Add a job to the job list */
int addjob(struct job_t *jobs, pid_t pid, int state, char *cmdline) 
{
    int i;
    
    if (pid < 1)
	return 0;

    for (i = 0; i < MAXJOBS; i++) {
	if (jobs[i].pid == 0) {
	    jobs[i].pid = pid;
	    jobs[i].state = state;
	    jobs[i].jid = nextjid++;
	    if (nextjid > MAXJOBS)
		nextjid = 1;
	    strcpy(jobs[i].cmdline, cmdline);
  	    if(verbose){
	        printf("Added job [%d] %d %s\n", jobs[i].jid, jobs[i].pid, jobs[i].cmdline);
            }
            return 1;
	}
    }
    printf("Tried to create too many jobs\n");
    return 0;
}

/* deletejob - Delete a job whose PID=pid from the job list */
int deletejob(struct job_t *jobs, pid_t pid) 
{
    int i;

    if (pid < 1)
	return 0;

    for (i = 0; i < MAXJOBS; i++) {
	if (jobs[i].pid == pid) {
	    clearjob(&jobs[i]);
	    nextjid = maxjid(jobs)+1;
	    return 1;
	}
    }
    return 0;
}

/* fgpid - Return PID of current foreground job, 0 if no such job */
pid_t fgpid(struct job_t *jobs) {
    int i;

    for (i = 0; i < MAXJOBS; i++)
	if (jobs[i].state == FG)
	    return jobs[i].pid;
    return 0;
}

/* getjobpid  - Find a job (by PID) on the job list */
struct job_t *getjobpid(struct job_t *jobs, pid_t pid) {
    int i;

    if (pid < 1)
	return NULL;
    for (i = 0; i < MAXJOBS; i++)
	if (jobs[i].pid == pid)
	    return &jobs[i];
    return NULL;
}

/* getjobjid  - Find a job (by JID) on the job list */
struct job_t *getjobjid(struct job_t *jobs, int jid) 
{
    int i;

    if (jid < 1)
	return NULL;
    for (i = 0; i < MAXJOBS; i++)
	if (jobs[i].jid == jid)
	    return &jobs[i];
    return NULL;
}

/* pid2jid - Map process ID to job ID */
int pid2jid(pid_t pid) 
{
    int i;

    if (pid < 1)
	return 0;
    for (i = 0; i < MAXJOBS; i++)
	if (jobs[i].pid == pid) {
            return jobs[i].jid;
        }
    return 0;
}

/* listjobs - Print the job list */
void listjobs(struct job_t *jobs) 
{
    int i;
    
    for (i = 0; i < MAXJOBS; i++) {
	if (jobs[i].pid != 0) {
	    printf("[%d] (%d) ", jobs[i].jid, jobs[i].pid);
	    switch (jobs[i].state) {
		case BG: 
		    printf("Running ");
		    break;
		case FG: 
		    printf("Foreground ");
		    break;
		case ST: 
		    printf("Stopped ");
		    break;
	    default:
		    printf("listjobs: Internal error: job[%d].state=%d ", 
			   i, jobs[i].state);
	    }
	    printf("%s", jobs[i].cmdline);
	}
    }
}
/******************************
 * end job list helper routines
 ******************************/

/******************************
 * Helper routines from csapp
 ******************************/
 
/*
 * Signal - wrapper for sigaction() from csapp
 */
handler_t *Signal(int signum, handler_t *handler) 
{
    struct sigaction action, old_action;

    action.sa_handler = handler;  
    sigemptyset(&action.sa_mask); /* Block sigs of type being handled */
    action.sa_flags = SA_RESTART; /* Restart syscalls if possible */

    if (sigaction(signum, &action, &old_action) < 0)
		unix_error("Signal error");
    return (old_action.sa_handler);
}

/*
 * Sigprocmask - wrapper for sigprocmask() from csapp
 */
void Sigprocmask(int how, const sigset_t *set, sigset_t *oldset)
{
    if (sigprocmask(how, set, oldset) < 0)
		unix_error("Sigprocmask error");
    return;
}

/*
 * Sigemptyset - wrapper for sigemptyset() from csapp
 */
void Sigemptyset(sigset_t *set)
{
    if (sigemptyset(set) < 0)
		unix_error("Sigemptyset error");
    return;
}

/*
 * Sigfillset - wrapper for sigfillset() from csapp
 */
void Sigfillset(sigset_t *set)
{ 
    if (sigfillset(set) < 0)
		unix_error("Sigfillset error");
    return;
}

/*
 * Sigaddset - wrapper for sigaddset() from csapp
 */
void Sigaddset(sigset_t *set, int signum)
{
    if (sigaddset(set, signum) < 0)
		unix_error("Sigaddset error");
    return;
}

/*
 * Sigdelset - wrapper for sigdelset() from csapp
 */
void Sigdelset(sigset_t *set, int signum)
{
    if (sigdelset(set, signum) < 0)
		unix_error("Sigdelset error");
    return;
}

/*
 * Sigsuspend - wrapper for sigsuspend() from csapp
 */
int Sigsuspend(const sigset_t *set)
{
    int rc = sigsuspend(set); /* always returns -1 */
    if (errno != EINTR)
        unix_error("Sigsuspend error");
    return rc;
}

/*
 * Kill - wrapper for kill() from csapp
 */
void Kill(pid_t pid, int signum) 
{
    int rc;
    if ((rc = kill(pid, signum)) < 0)
		unix_error("Kill error");
}

/*
 * Setpgid - wrapper for setpgid() from csapp
 */
void Setpgid(pid_t pid, pid_t pgid) {
    int rc;

    if ((rc = setpgid(pid, pgid)) < 0)
	unix_error("Setpgid error");
    return;
}

/* 
 * sio_reverse - Reverse a string (from K&R) 
 * from csapp
 */
static void sio_reverse(char s[])
{
    int c, i, j;

    for (i = 0, j = strlen(s)-1; i < j; i++, j--) {
        c = s[i];
        s[i] = s[j];
        s[j] = c;
    }
}

/* 
 * sio_ltoa - Convert long to base b string (from K&R) 
 * from csapp
 */
static void sio_ltoa(long v, char s[], int b) 
{
    int c, i = 0;
    int neg = v < 0;

    if (neg)
	v = -v;

    do {  
        s[i++] = ((c = (v % b)) < 10)  ?  c + '0' : c - 10 + 'a';
    } while ((v /= b) > 0);

    if (neg)
	s[i++] = '-';

    s[i] = '\0';
    sio_reverse(s);
}

/* 
 * sio_strlen - Return length of string (from K&R) 
 * from csapp
 */
static size_t sio_strlen(char s[])
{
    int i = 0;

    while (s[i] != '\0')
        ++i;
    return i;
}

/* 
 * sio_puts - safe string output from csapp
 */
ssize_t sio_puts(char s[]) /* Put string */
{
    return write(STDOUT_FILENO, s, sio_strlen(s));
}

/* 
 * sio_putl - safe integer output from csapp
 */
ssize_t sio_putl(long v) /* Put long */
{
    char s[128];
    sio_ltoa(v, s, 10); /* Based on K&R itoa() */
    return sio_puts(s);
}

/* 
 * sio_error - safe error function from csapp
 */
void sio_error(char s[]) /* Put error message and exit */
{
    sio_puts(s);
    _exit(1);
}

/* 
 * Sio_putl - wrapper for sio_putl() from csapp
 */
ssize_t Sio_putl(long v)
{
    ssize_t n;
    if ((n = sio_putl(v)) < 0)
		sio_error("Sio_putl error");
    return n;
}

/* 
 * Sio_puts - wrapper for sio_puts() from csapp
 */
ssize_t Sio_puts(char s[])
{
    ssize_t n;
    if ((n = sio_puts(s)) < 0)
		sio_error("Sio_puts error");
    return n;
}

/* 
 * Sio_error - wrapper for sio_error() from csapp
 */
void Sio_error(char s[])
{
    sio_error(s);
}

/***********************
 * Other helper routines
 ***********************/

/*
 * usage - print a help message
 */
void usage(void) 
{
    printf("Usage: shell [-hvp]\n");
    printf("   -h   print this message\n");
    printf("   -v   print additional diagnostic information\n");
    printf("   -p   do not emit a command prompt\n");
    exit(1);
}

/*
 * unix_error - unix-style error routine
 */
void unix_error(char *msg)
{
    fprintf(stdout, "%s: %s\n", msg, strerror(errno));
    exit(1);
}

/*
 * app_error - application-style error routine
 */
void app_error(char *msg)
{
    fprintf(stdout, "%s\n", msg);
    exit(1);
}

/*
 * sigquit_handler - The driver program can gracefully terminate the
 *    child shell by sending it a SIGQUIT signal.
 */
void sigquit_handler(int sig) 
{
    printf("Terminating after receipt of SIGQUIT signal\n");
    exit(1);
}

