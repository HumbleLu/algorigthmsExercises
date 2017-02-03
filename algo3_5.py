
class Edge:
    def __init__(self, v1, v2, weight):
        if v1 > v2:
            self.vertices = [v2, v1]
        else:
            self.vertices = [v1, v2]

        self.weight = weight

    def __eq__(self, other):
        """Override the default Equals behavior"""
        return (self.vertices == other.vertices and self.weight == other.weight)

    def __ne__(self, other):
        """Define a non-equality"""
        return not self.__eq__(other)

    def __str__(self):
        return str(self.vertices[0]) + "-" + str(self.vertices[1])



class Graph:
    def __init__(self):
        self.vertices = []
        self.edges = []

    def addEdge(self, e):
        if e not in self.edges:
            self.edges.append(e)

        for v in e.vertices:
            if v not in self.vertices:
                self.vertices.append(v)

        self.edges.sort(key = lambda x: x.vertices[1])
        self.edges.sort(key = lambda x: x.vertices[0])
        self.vertices.sort()

    def search_edge(self, v):
        ret = []
        for e in self.edges:
            if v in e.vertices:
                ret.append(e)
        ret.sort()
        return ret

    def __str__(self):
        return reduce(lambda x,y: x.__str__() + " " + y.__str__(), self.edges)

    def vertices_str(self):
        return str(self.vertices[0]) + ": " + \
               reduce(lambda x,y: str(x) + " " + str(y), self.vertices)

    def search_link(self, comp1, comp2):
        edegs_cand = []

        if comp1 == comp2:
            return []

        for v1 in comp1:
            for e in self.search_edge(v1):
                for v2 in comp2:
                    if v2 in e.vertices:
                        edegs_cand.append(e)

        edegs_cand.sort(key = lambda e: e.vertices[1])
        edegs_cand.sort(key=lambda e: e.vertices[0])
        edegs_cand.sort(key=lambda e: e.weight)

        return edegs_cand

    def boruvka_search(self):
        def print_comp(components):
            for c in components:
                print str(c[0]) + ": " + str(
                    reduce(lambda x, y: str(x) + " " + str(y), sorted(c)))

        def min_value(list):
            if len(list) == 0:
                return list
            else:
                return min(list)

        def shorted_edge(edge_list):
            if len(edge_list) == 0:
                return edge_list
            else:
                edge_list.sort(key=lambda e: e.vertices[1])
                edge_list.sort(key=lambda e: e.vertices[0])
                edge_list.sort(key=lambda e: e.weight)
                return edge_list[0]

        edges = []
        comp_edge = {}
        components = [sorted([v]) for v in self.vertices]
        print_comp(components)

        used_edges = []
        while(len(components) != 1):
            for c in components:
                links = [self.search_link(c, other) for other in components]

                weights = [[e.weight for e in self.search_link(c, other)] for other
                           in components]

                # get intake component
                min_weights = [min_value(w) for w in weights]
                intake_index =  min_weights.index(min(min_weights))

                # get intake edge
                shortest_edge = shorted_edge(links[intake_index])
                comp_edge[str(c)] = shortest_edge
                if shortest_edge not in edges:
                    edges.append(shortest_edge)

            new_components = []

            for e in edges:
                if e not in used_edges:
                    linked_components = [comp for comp in components if len(
                        list(set(comp) & set(e.vertices))) > 0]

                    linked_components.sort(key = lambda s: s[0])
                    linked_components.sort(key = lambda s: len(s), reverse = True)

                    #print e
                    #print linked_components

                    if len(linked_components) == 2:
                        c = linked_components[0] + linked_components[1]
                        new_components.append(c)
                        components.remove(linked_components[0])
                        components.remove(linked_components[1])

                    else:
                        linked_newcomponents = [comp for comp in new_components if
                                             len(list(set(comp) & set(e.vertices))) > 0]

                        #print "linked_newcomp: " + str(linked_newcomponents)

                        linked_newcomponents.sort(key=lambda s: s[0])
                        linked_newcomponents.sort(key=lambda s: len(s),
                                               reverse=True)

                        if len(linked_newcomponents) == 1:
                            insert_ind = new_components.index(
                                linked_newcomponents[0])
                            if len(linked_newcomponents[0]) >= len(
                                    linked_components[0]):
                                new_components[insert_ind] += [v for v in
                                                               linked_components[0] if v
                                not in new_components[insert_ind]]
                                components.remove(linked_components[0])
                            else:
                                new_components[insert_ind] = \
                                    linked_components[0] + [v for v in
                                                            linked_newcomponents[0] if v not in linked_components[0]]

                        if len(linked_newcomponents) == 2:
                            insert_ind = new_components.index(linked_newcomponents[0])
                            new_components[insert_ind] = \
                                linked_newcomponents[0] + \
                                [v for v in linked_newcomponents[1] if v not
                                 in linked_newcomponents[0]]
                            new_components.remove(linked_newcomponents[1])

                    #print "append: "+ str(new_components)
                    #new_components = merge(new_components)
                    #print new_components
                    used_edges.append(e)


            #print "----"
            #new_components = sorted(new_components, key=lambda arr: arr[0])
            #print new_components

            components = new_components
            components = sorted(components, key=lambda arr: arr[0])
            # print new_components
            #print components
            #print "----"

            print_comp(sorted(components))

        edges.sort(key=lambda e: e.vertices[1])
        edges.sort(key=lambda e: e.vertices[0])
        print reduce(lambda x,y: x + " " + y, [e.__str__() for e in edges])

c = Graph()

file_path = 'input.txt'
f = open(file_path, 'r')
for line in open(file_path):
    line = f.readline()
    c.addEdge(Edge(int(line.split(" ")[0]), int(line.split(" ")[1]), \
                                                 int(line.split(" ")[2])))
f.close()


c.boruvka_search()



#aa = [[21, 41, 46, 43, 0, 39], [1, 36, 17, 7, 28, 12, 31, 3, 9, 34, 44],
# [2, 18, 20, 49, 30, 8, 32, 13], [1, 36, 17, 7, 28, 12, 31, 3, 4, 16, 26], [5, 19, 6, 35, 9, 34, 44], [10, 48, 42, 23, 14, 22], [5, 19, 6, 35, 11, 25, 47, 33], [15, 29, 40, 24, 45, 21, 41, 46, 43], [21, 41, 46, 43, 8, 32, 13], [8, 32, 13, 37, 38, 27]]
#print merge(aa)

