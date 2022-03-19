def bracket_search(input_string: str):
    bracket_stack = []
    current_index = 0
    opening = ["{", "[", "("]
    closing = ["}", "]", ")"]

    while current_index < len(input_string):
        current_char = input_string[current_index]
        if current_char in opening:
            bracket_stack += [current_char]
        elif current_char in closing:
            index = closing.index(current_char)
            matching_bracket = opening[index]

            if (
                len(bracket_stack) == 0
                or bracket_stack[len(bracket_stack) - 1] != matching_bracket
            ):
                print(
                    "Error, incorrectly placed bracket at index %s, %s in '%s'"
                    % (current_index, current_char, input_string)
                )
                return
            bracket_stack.pop()
        current_index += 1

    print("The string %s has all brackets matched well" % input_string)


bracket_search("{ foo bar(a,b,c)} [d,e,f] {{abcdefg}} ...... (})")
