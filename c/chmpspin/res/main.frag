#version 150

uniform mat4 model;
uniform vec3 light;

in vec4 f_position;
in vec3 f_normal;

void main() {
    mat3 normalMatrix = transpose(inverse(mat3(model)));

    vec3 normal = normalize(normalMatrix * f_normal);
    vec4 position = model * f_position;

    vec3 dist = normalize(light - vec3(position));
    vec4 diffuse = vec4(1, 1, 1, 1) * max(0.1, dot(normal, dist));
    vec4 specular = vec4(1, 1, 1, 1) * clamp(pow(max(0.0, dot(reflect(normal, dist), vec3(position))), 0.9), 0, 1);

    gl_FragColor = diffuse;
}
