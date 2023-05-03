package com;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Assignment: CSC375 Assignment 2
 * Author: Vitaliy Shydlonok
 * Date: 10/28/2019
 *
 * My implementation of a concurrent list by using a mutex lock.
 */

public class ConcurrentList<T> implements List<T> {

    private Semaphore mutex = new Semaphore(1); // Semaphore for locking the array
    private volatile Object[] array = new Object[100]; // Array of elements
    private volatile int numElements = 0; // The number of elements in the array
    private volatile int numSpaces = 100; // The available space allocated for the array

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        boolean ret = false;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Loop through the array until the element is found
                for(int i=0; i<numElements; i++) {
                    if (array[i] == o) {
                        ret = true;
                        break;
                    }
                }
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Iterator<T> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = null;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Create a new array and copy the contents of the current array to it
                arr = new Object[size()];
                System.arraycopy(array, 0, arr, 0, numElements);
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return arr;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Check if the available space has run out
                if (numSpaces == numElements) {
                    // Copy the array to a new one that is twice the size
                    Object[] arr = array;
                    array = new Object[numSpaces*2];
                    System.arraycopy(arr, 0, array, 0, numElements);
                    numSpaces *= 2;
                }
                // Add the element to the end of the array
                array[numElements] = t;
                numElements++;
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean ret = false;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Loop through the array until the element is found
                for(int i=0; i<numElements; i++) {
                    if (array[i] == o) {
                        // Shift all elements after the current index to the left by one space
                        System.arraycopy(array, i+1, array, i, numElements);
                        ret = true;
                        numElements --;
                        break;
                    }
                }
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Reset the number of elements
                numElements = 0;
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T get(int index) {
        // Make sure that the index is within the array
        if (index < 0 || index >= numElements)
            throw new IndexOutOfBoundsException();

        Object item = null;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Retrieve the element at index
                item = array[index];
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (T)item;
    }

    @Override
    public T set(int index, T element) {
        // Make sure that the index is within the array
        if (index < 0 || index >= numElements)
            throw new IndexOutOfBoundsException();

        try {
            // Lock the array
            mutex.acquire();
            try {
                // Overwrite the element at index
                array[index] = element;
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return element;
    }

    @Override
    public void add(int index, T element) {
        // Make sure that the index is within the array
        if (index < 0 || index >= numElements)
            throw new IndexOutOfBoundsException();

        try {
            // Lock the array
            mutex.acquire();
            try {
                // Check if the available space has run out
                if (numSpaces == numElements) {
                    // Copy the array to a new one that is twice the size
                    Object[] arr = array;
                    array = new Object[numSpaces*2];
                    System.arraycopy(arr, 0, array, 0, numElements);
                    numSpaces *= 2;
                }

                // Shift all elements at and after index one space to the right
                System.arraycopy(array, index, array, index+1, numElements-index-1);

                // Replace the element at index
                array[index] = element;
                numElements++;
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T remove(int index) {
        // Make sure that the index is within the array
        if (index < 0 || index >= numElements)
            throw new IndexOutOfBoundsException();

        Object ret = null;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Retrieve the element at index
                ret = array[index];

                // Shift all elements after index one space to the left
                System.arraycopy(array, index+1, array, index, numElements-index-1);
                numElements --;
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (T)ret;
    }

    @Override
    public int indexOf(Object o) {
        int ret = -1;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Loop through the array until the element is found
                for(int i=0; i<numElements; i++) {
                    if (array[i] == o) {
                        ret = i;
                        break;
                    }
                }
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public int lastIndexOf(Object o) {
        int ret = -1;
        try {
            // Lock the array
            mutex.acquire();
            try {
                // Loop backwards through the array until the element is found
                for(int i=numElements-1; i>=0; i--) {
                    if (array[i] == o) {
                        ret = i;
                        break;
                    }
                }
            } finally {
                // Unlock the array
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
