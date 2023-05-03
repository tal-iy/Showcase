#pragma once

template <class T>
class TreeNode
{
	public:
		T value;           // The value in the node
		TreeNode *left;    // Pointer to left child node
		TreeNode *right;   // Pointer to right child node

	public:
		TreeNode() { left = nullptr; right = nullptr; };
};