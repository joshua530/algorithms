from linked_list import LinkedList

def merge_sort(linked_list):
    """
    Sorts a linked list in ascending order
    - Recursively splits linked list into individual nodes
    - Repeatedly combines the nodes till only one list is left

    Returns the sorted linked list
    """

    if linked_list.size == 1 or linked_list.head is None:
        return linked_list

    left, right = split(linked_list)
    left = merge_sort(left)
    right = merge_sort(right)

    return combine(left, right)

def split(linked_list):
    """
    Divides a linked list into two parts along the middle
    """
    if linked_list.size == 0 or linked_list is None:
        left_half = linked_list
        right_half = None
        return left_half, right_half
    
    midpoint = linked_list.size // 2
    mid_node = linked_list.node_at_index(midpoint-1)

    right_half = LinkedList()

    left_half = linked_list
    right_half.head = linked_list.node_at_index(midpoint)
    mid_node.next_node = None # break off right half from parent list
    print(left_half, '-----',right_half)
    
    return left_half, right_half

def combine(left, right):
    """
    Joins two linked lists, sorting them in the process

    Returns a sorted linked list resulting from the merging 
    of the two linked lists. Nodes are sorted by the data
    contained in them
    """

    sorted_list = LinkedList() # will contain combined and sorted data from the two linked lists
    sorted_list.add(None) # add dummy head to enable addition of other nodes to this list

    # nodes to hold current iterative position
    sorted_node = sorted_list.head
    right_node = right.head
    left_node = left.head

    # iterate over the lists till one of them becomes empty
    while right_node and left_node:
        if left_node.data < right_node.data:
            sorted_node.next_node = left_node
            sorted_node = sorted_node.next_node
            left_node = left_node.next_node
        else:
            sorted_node.next_node = right_node
            sorted_node = sorted_node.next_node
            right_node = right_node.next_node

    # check for remaining nodes
    while left_node:
        sorted_node.next_node = left_node
        sorted_node = sorted_node.next_node
        left_node = left_node.next_node
    while right_node:
        sorted_node.next_node = right_node
        sorted_node = sorted_node.next_node
        right_node = right_node.next_node

    sorted_list.head = sorted_list.head.next_node
    return sorted_list
