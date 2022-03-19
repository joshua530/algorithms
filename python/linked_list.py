class Node:
    """
    A simple node representation
    class properties:
        - data -> the data the node contains
        - next_node -> the node that follows the current node
    """

    data = None
    next_node = None

    def __init__(self, data):
        self.data = data

    def __repr__(self):
        return "<Node data: {}>".format(self.data)


class LinkedList:
    """
    Singly linked list
    """

    def __init__(self):
        self.head = None

    def is_empty(self):
        return self.head is None

    @property
    def size(self):
        """
        Returns the number of nodes in the list
        takes O(n) time
        """
        current = self.head
        size = 0

        while current:
            size += 1
            current = current.next_node

        return size

    def add(self, data):
        """
        Reassigns head node
        Takes O(1) time[the operations that happen are setting the head and next node]
        """
        new_node = Node(data)
        new_node.next_node = self.head
        self.head = new_node

    def __repr__(self):
        """
        Return string implementation of the linked list
        Takes O(n) time
        """
        nodes = []
        current = self.head

        while current:
            if current is self.head:
                nodes.append("[Head: {}]".format(current.data))
            elif current.next_node is None:
                nodes.append("[Tail: {}]".format(current.data))
            else:
                nodes.append("[{}]".format(current.data))

            current = current.next_node

        return "-> ".join(nodes)

    def search(self, data):
        """
        Searches for node containing matching data
        If no node containing matching data is found, None is returned

        Runs in O(n) time
        """
        current = self.head

        while current:
            if current.data == data:
                return current
            else:
                current = current.next_node
        return None

    def insert(self, data, index):
        """
        Insert a new node at the given index in the linked list
        Insertion takes O(1) time but searching for the index takes O(n) time

        Therefore overall time taken is O(n)
        """
        if index == 0:
            self.add(data)
            return

        node = Node(data)
        prev_node_position = index - 1
        current_node = self.head
        current_index = 0

        while current_index < prev_node_position:
            current_index += 1
            current_node = current_node.next_node

        node.next_node = current_node.next_node
        current_node.next_node = node

    def remove(self, key):
        """
        Find first node with data that matches given key. Delete and return the
        deleted node if it exists. Return None if the key doesn't exist.
        Takes O(1) time to reallocate node pointers and O(n) time to find the node
        with the given key

        Therefore it overally takes O(n) time to delete the node
        """
        current_node = self.head
        previous_node = None            

        while current_node is not None:
            if current_node is self.head and current_node.data == key:
                self.head = current_node.next_node
                break
            elif current_node.data == key:
                previous_node.next_node = current_node.next_node
                break
            
            previous_node = current_node
            current_node = current_node.next_node

        return current_node

    def remove_at_index(self, index):
        """
        Find node at given index. If found, delete and return it. If not found,
        return None.

        Takes O(n) time to find the node at the given index and O(1) time to delete the
        node.
        Overall time is therefore O(n) time
        """
        current_index = 0
        current_node = self.head
        previous_node = None

        while current_node is not None:
            # remove head node
            if current_node is self.head and index == 0:
                self.head = current_node.next_node
                break
            elif current_index == index:
                # item found, replace it and break out of the loop
                previous_node.next_node = current_node.next_node
                break
            
            previous_node = current_node
            current_node = current_node.next_node
            current_index += 1

        return current_node

    def node_at_index(self, index):
        if index == 0:
            return self.head

        position = 0
        current = self.head
        while position < index:
            current = current.next_node
            position += 1

        return current

    def prepend_node(self, node):
        self.add(node.data)

    def pop(self):
        """
        Remove node at the end

        The node before the final node is obtained. Its next node
        value is then set to None
        """
        size = self.size
        before_to_remove = self.node_at_index(size - 2)
        before_to_remove.next_node = None
