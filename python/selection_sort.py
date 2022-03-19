def selection_sort(numbers: list):
    sorted_numbers = []

    while numbers:
        min_num_index = None
        for i in range(len(numbers)):
            if min_num_index is None:
                min_num_index = i
            else:
                if numbers[i] < numbers[min_num_index]:
                    min_num_index = i
        sorted_numbers.append(numbers.pop(min_num_index))

    return sorted_numbers
