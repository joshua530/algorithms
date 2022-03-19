def merge_sort(values):
    if len(values) <= 1: # empty or list has one value
        return values

    midpoint = len(values) // 2
    sorted_list = []
    right_list = merge_sort(values[:midpoint])
    left_list = merge_sort(values[midpoint:])

    left_position = 0
    right_position = 0

    while left_position < len(left_list) and right_position < len(right_list):
        if left_list[left_position] < right_list[right_position]:
            sorted_list.append(left_list[left_position])
            left_position += 1
        else:
            sorted_list.append(right_list[right_position])
            right_position += 1

    sorted_list.append(left_list[left_position:])
    sorted_list.append(right_list[right_position:])

    return sorted_list
