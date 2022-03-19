# recursive binary search


def binary_search_recursive(the_list, target):
    if len(the_list) == 0:
        return False

    middle = (len(the_list)) // 2
    if the_list[middle] == target:
        return True
    elif the_list[middle] < target:
        return binary_search_recursive(the_list[middle + 1 :], target)
    elif the_list[middle] > target:
        return binary_search_recursive(the_list[:middle], target)
