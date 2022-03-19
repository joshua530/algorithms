class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

    def __str__(self) -> str:
        return str(self.data)

    def __repr__(self) -> str:
        return str(self.data)


def reverse_nodes(root: Node):
    """
    Reverses all nodes and returns the first node ie the last node
    in the unreversed linked list
    """
    current = root
    prev = None
    while current.next is not None:
        tmp = current.next
        current.next = prev
        prev = current
        current = tmp
    current.next = prev
    return current


def print_nodes(root_node: Node):
    current = root_node
    while current.next != None:
        print("%s -> " % (current), end="")
        current = current.next
    print(current)


x = Node(1)

tmp = x
for i in range(2, 6):
    tmp.next = Node(i)
    tmp = tmp.next

print_nodes(x)

print_nodes(reverse_nodes(x))
