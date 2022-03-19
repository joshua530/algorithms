def selection_sort(items:list):
    for i in range(len(items)-1):
        if items[i] != -1:
            least_index = i
            for j in range(i+1, len(items)):
                if items[j] != -1 and items[j] < items[least_index]:
                    least_index = j
            tmp = items[i]
            items[i] = items[least_index]
            items[least_index] = tmp	

nums = [3,2,8,23,3,2,7,-1, 0,-1,43,2,76,3,65,2,-1,4,76]
selection_sort(nums)
print(nums)
