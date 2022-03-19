def insertion_sort(to_sort:list):
    for i in range(len(to_sort)):
        for j in range(i, 0, -1):
            if to_sort[j] < to_sort[j-1]:
                tmp = to_sort[j]
                to_sort[j] = to_sort[j-1]
                to_sort[j-1] = tmp

items = [2,1,10,6,3,4,5]
print("Before="+str(items))
insertion_sort(items)

print("After="+str(items))
