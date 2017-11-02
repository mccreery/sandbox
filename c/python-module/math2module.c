#include "Python.h"

static PyObject *wave(PyObject *self, PyObject *args) {
	float x;
	if(!PyArg_ParseTuple(args, "x", &x)) return NULL;
	x *= 255.0f;
	
}

static PyObject *pot(PyObject *self, PyObject *args) {
	int x;
	if(!PyArg_ParseTuple(args, "i", &x)) return NULL;
	return PyLong_FromLong(1 << x);
}

static PyObject *palindrome(PyObject *self, PyObject *args) {
	char *text;
	if(!PyArg_ParseTuple(args, "s", &text)) return NULL;

	for(char *right = text + strlen(text) - 1; text < right; text++, right--) {
		if(*text != *right) {
			Py_RETURN_FALSE;
		}
	}
	Py_RETURN_TRUE;
}

static PyMethodDef keywdarg_methods[] = {
	{"pot", pot, METH_VARARGS, "Returns the specified power-of-two."},
	{"palindrome", palindrome, METH_VARARGS, "Returns True if the given string is a palindrome."},
	{NULL, NULL, 0, NULL}   /* sentinel */
};

static struct PyModuleDef keywdargmodule = {
	PyModuleDef_HEAD_INIT,
	"math2",
	NULL,
	-1,
	keywdarg_methods
};

PyMODINIT_FUNC PyInit_math2(void) {
	
	return PyModule_Create(&keywdargmodule);
}