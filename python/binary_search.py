# binary search implementation


def binary_search(the_list, target):
    begin_index = 0
    end_index = len(the_list) - 1

    while begin_index <= end_index:
        midpoint = (begin_index+end_index) // 2

        if the_list[midpoint] == target:
            return midpoint
        elif the_list[midpoint] < target:
            begin_index = midpoint + 1
        elif the_list[midpoint] > target:
            end_index = midpoint - 1
    return None
