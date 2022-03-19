def heapify(items:list):
    the_heap = []

    for i in items:
        the_heap.append(i)
        bubble_up(the_heap, len(the_heap)-1)

    return the_heap

def swap(heap, a, b):
    tmp = heap[a]
    heap[a] = heap[b]
    heap[b] = tmp

def bubble_up(heap, index):
    parent = (index-1)//2

    if parent >= 0:
        if heap[parent] > heap[index]:
            swap(heap, parent, index)
            bubble_up(heap, parent)

def bubble_down(heap, index):
    least_child = min_child(heap, index)

    if least_child != -1:
        if heap[least_child] < heap[index]:
            swap(heap, index, least_child)
            bubble_down(heap, least_child)

def min_child(heap, index):
    right = index*2+2
    left = index*2+1

    if right < len(heap):
        if heap[right] < heap[left]:
            return right

    if left < len(heap):
        return left

    return -1

def extract_min(heap):
    to_extract = None
    if len(heap) > 1:
        to_extract = heap[0]
        print(to_extract)
        swap(heap, 0, len(heap)-1)
        heap.pop(len(heap)-1)
        bubble_down(heap, 0)
    bubble_down(heap, 0) # ensure the new parent item conforms to the heap invariant
    return to_extract

items = [4,6,8,2,10,5,1]
the_heap = heapify(items)
print(the_heap)
print(extract_min(the_heap))
print(the_heap)
