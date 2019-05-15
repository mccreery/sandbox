def selection_sort(xs):
    for i in range(len(xs)):
        j = min(range(i, len(xs)), key=xs.__getitem__)
        xs[i], xs[j] = xs[j], xs[i]
    return xs
