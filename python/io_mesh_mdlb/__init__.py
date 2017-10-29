bl_info = {
	"name": "Binary MDL",
	"author": "Sam McCreery",
	"blender": (2, 7, 6),
	"version": (1, 0, 0),
	"location": "File > Import-Export",
	"description": "Export Binary MDL",
	"category": "Import-Export"
}

import bpy, struct
from bpy_extras.io_utils import ExportHelper
from bpy.props import BoolProperty

class ExportMyFormat(bpy.types.Operator, ExportHelper):
	bl_idname = "export_mdl.mdl"
	bl_label = "MDL Exporter"
	bl_options = {"PRESET"}

	filename_ext = ".mdl"

	use_normals = BoolProperty(
		name="Normals",
		description="Export vertex normals",
		default=True)
	use_gzip = BoolProperty(
		name="GZip",
		description="Use GZip compression",
		default=True)
	use_uvs = BoolProperty(
		name="UVs",
		description="Export UV coordinates",
		default=True)

	def draw(self, context):
		self.layout.prop(self, "use_normals")
		self.layout.prop(self, "use_gzip")
		self.layout.prop(self, "use_uvs")

	def execute(self, context):
		obj = bpy.context.scene.objects.active
		if obj.type != "MESH":
			raise Exception("Object must be a mesh")

		mesh = obj.data

		file = open(self.filepath, "wb")

		file.write("MDL".encode("UTF-8"))
		file.write(struct.pack("B", self.use_gzip))

		if self.use_gzip:
			file.close()

			file_temp = open(self.filepath, "ab")
			import gzip
			file = gzip.GzipFile("", "ab", fileobj=file_temp)

		file.write(struct.pack("B", len(mesh.name)))
		file.write(mesh.name.encode())

		file.write(struct.pack("B", 1 + self.use_normals + self.use_uvs))
		file.write(("f3" + "fn" * self.use_normals + "fu" * self.use_uvs).encode())

		file.write(struct.pack(">H", len(mesh.vertices)))
		for vertex in mesh.vertices:
			file.write(struct.pack(">fff", vertex.co.x, vertex.co.y, vertex.co.z))

			if self.use_normals:
				file.write(struct.pack(">fff", *vertex.normal))

			if self.use_uvs:
				uv = mesh.uv_layers.active.data[polygon.loop_indices[i]].uv or (0.0, 0.0)
				file.write(struct.pack(">ff", *uv))

		triangles = []
		for polygon in mesh.polygons:
			for i in range(1, len(polygon.vertices) - 1):
				triangles.append((polygon.vertices[0], polygon.vertices[i], polygon.vertices[i + 1]))

		file.write(struct.pack(">H", len(triangles)))
		for triangle in triangles:
			file.write(struct.pack(">HHH", triangle[0], triangle[1], triangle[2]))

		file.close()
		if self.use_gzip:
			file_temp.close()
		return {"FINISHED"}

def menu_func(self, context):
	self.layout.operator(ExportMyFormat.bl_idname, text="Binary MDL (.mdl)")

def register():
	bpy.utils.register_module(__name__);
	bpy.types.INFO_MT_file_export.append(menu_func)

def unregister():
	bpy.utils.unregister_module(__name__);
	bpy.types.INFO_MT_file_export.remove(menu_func)

if __name__ == "__main__":
	register()
