def quick_sort(values: list):
    if len(values) <= 1: # terminating condition(base case)
        return values

    less_than_pivot = []
    greater_than_pivot = []
    pivot = values[0]

    for i in range(1, len(values)):
        if values[i] < pivot:
            less_than_pivot.append(values[i])
        else:
            greater_than_pivot.append(values[i])

    return quick_sort(less_than_pivot) + [pivot] + quick_sort(greater_than_pivot)
