def permute(to_permute):
    if len(to_permute) == 1:
        return [to_permute[:]]
    
    items = []
    
    for i in to_permute:
        print(to_permute)
        num = to_permute.pop(0)
        permutations = permute(to_permute)
        
        for permutation in permutations:
            permutation.append(num)
            print("p={}".format(permutation))
            
        items.extend(permutations)
        to_permute.append(num)
        
    return items
    
print(permute([1,2,3]))
