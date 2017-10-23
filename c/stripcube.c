/* Data to draw a triangle strip which forms a cube */

const float min = -1.f, max = 1.f;
const float verts[24] = {
	min, max, min,
	max, max, min,
	min, max, max,
	max, max, max,
	min, min, min,
	max, min, min,
	min, min, max,
	max, min, max
};
const int strip[14] = {1, 0, 3, 2, 6, 0, 4, 1, 5, 3, 7, 6, 5, 4};
