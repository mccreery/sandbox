from distutils.core import setup, Extension

math2 = Extension('math2', sources = ['math2module.c'])
setup(name = 'Math2', version = '1.0', description = 'Math but better', ext_modules = [math2])