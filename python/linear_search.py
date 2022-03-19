# linear search implementation



def linear_search(the_list, target):
    for i in range(0, len(the_list)):
        if the_list[i] == target:
            return i
    return None
