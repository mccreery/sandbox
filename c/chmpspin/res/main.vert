#version 150

uniform mat4 model;
uniform mat4 projection;

in vec4 pos;
in vec3 normal;

out vec4 f_position;
out vec3 f_normal;

void main() {
    gl_Position = projection * model * pos;

    // Pass-through
    f_position = pos;
    f_normal = normal;
}
