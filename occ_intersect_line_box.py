from OCC.Core.BRepBuilderAPI import BRepBuilderAPI_MakeEdge
from OCC.Core.BRepBuilderAPI import BRepBuilderAPI_MakeWire
from OCC.Core.BRepBuilderAPI import BRepBuilderAPI_MakeFace
from OCC.Core.gp import gp_Pnt, gp_Vec, gp_Dir, gp_Lin
from OCC.Core.IntCurvesFace import IntCurvesFace_ShapeIntersector
from OCC.Core.BRepPrimAPI import BRepPrimAPI_MakePrism
import json
import sys
from OCC.Extend.DataExchange import write_step_file

count = 0
def intersect(json1,json2):
    global count
    count += 1
    startPoint = gp_Pnt(json1[0]['x'], json1[0]['y'], json1[0]['z'])
    endPoint = gp_Pnt(json1[1]['x'], json1[1]['y'], json1[1]['z'])
    thickness = json2[-1]['startPoint']['z']

    mkWire = BRepBuilderAPI_MakeWire()

    for i in range(0,len(json2)-1):
        item = json2[i]
        s = item['startPoint']
        e = item['endPoint']
        edge1 = BRepBuilderAPI_MakeEdge(gp_Pnt(s['x'],s['y'],s['z']), gp_Pnt(e['x'],e['y'],e['z'])).Edge()
        mkWire.Add(edge1)
    wire = mkWire.Wire()
    face = BRepBuilderAPI_MakeFace(wire).Face()
    prism = BRepPrimAPI_MakePrism(face, gp_Vec(0., 0., thickness))
    shape = prism.Shape()

    line = gp_Lin(startPoint, gp_Dir(gp_Vec(startPoint, endPoint)))

    intersector = IntCurvesFace_ShapeIntersector()
    intersector.Load(shape, 1e-3)

    intersector.Perform(line, 0, endPoint.Distance(startPoint))
    if intersector.IsDone():
        nb_results = intersector.NbPnt()
        print(f"results: {nb_results}")
        for i in range(1, nb_results+1):
            intersection_point = intersector.Pnt(i)
            print(f"Intersection point {i}: {intersection_point.X()}, {intersection_point.Y()}, {intersection_point.Z()}")
        print()
    #write_step_file(shape, f"shape{count}.stp")

with open(sys.argv[1], 'r', encoding='utf-8') as file:
    a = None
    b = None
    for line in file:
        str = line.strip()
        if str.startswith('line:'):
            a = str.removeprefix('line:')
        elif str.startswith('polygonList:'):
            b = str.removeprefix('polygonList:')
        if a and b:
            intersect(json.loads(f"[{a}]"),json.loads(b))
            a = None
            b = None
        


