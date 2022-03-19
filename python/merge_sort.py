def merge_sort(the_list):
    """
    Sorts a given list in ascending order

    Divide: find midpoint of the list and split it into two sublists
    Conquer: recursively sort the sublists
    Combine: merge all the sublists into one sorted list

    Takes O(n log n) time
    Space complexity is O(n)
    """

    if len(the_list) <= 1:
        return the_list

    left_half, right_half = split(the_list)
    left = merge_sort(left_half)
    right = merge_sort(right_half)

    return merge(left, right)

def split(the_list):
    """
    Divides an unsorted list into two sublists along the middle of the list
    Returns the divided sublists, left and right

    Takes overall O(log n) time
    """

    midpoint = len(the_list)//2
    left, right = the_list[:midpoint], the_list[midpoint:]

    return left, right

def merge(left, right):
    """
    Merges two arrays, sorting them in the process
    Returns the merged list

    Takes overall O(n) time
    """
    l = [] # the sorted items will be stored here
    i = 0 # keeps track of position in left array
    j = 0 # keeps track of position in right array

    while i < len(left) and j < len(right):
        if left[i] < right[j]:
            l.append(left[i])
            i+=1
        else: # > | ==
            l.append(right[j])
            j+=1

    # one of the arrays does not have its values completely iterated through
    # assumption is values have already been sorted
    while i < len(left):
        l.append(left[i])
        i+=1

    while j < len(right):
        l.append(right[j])
        j+=1

    return l
