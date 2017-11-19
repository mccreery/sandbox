layout (location = 0) in vec3 pos;
layout (location = 3) in vec3 normal;

out vec3 posio;
out vec3 normalio;

int main() {
	posio = pos;
	normalio = normal;
}
