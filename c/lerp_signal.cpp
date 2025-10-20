#include <array>
#include <cmath>
#include <iostream>
#include <iterator>
#include <type_traits>

struct Point {
    float x;
    float y;

    Point operator*(float b) {
        return Point{x * b, y * b};
    }

    Point operator+(Point b) {
        return Point{x + b.x, y + b.y};
    }
};

template <class Stream>
Stream& operator<<(Stream& stream, Point point) {
    return stream << '(' << point.x << ", " << point.y << ')';
}

template <class T, class F>
T lerp(T a, T b, F t) {
    return a * (1 - t) + b * t;
}

template <class F>
F repeat(F x, F max) {
    return x - max * std::floor(x / max);
}

template <class Iterator, class F>
auto lerp_signal(Iterator begin, Iterator end, F t) {
    using Difference = typename std::iterator_traits<Iterator>::difference_type;
    Difference n = std::distance(begin, end);

    t = repeat(t, static_cast<F>(n));
    F integral;
    F fractional = std::modf(t, &integral);

    Difference i = integral;
    Iterator current = std::next(begin, i);
    Iterator next = std::next(begin, (i + 1) % n);

    return lerp(*current, *next, fractional);
}

int main() {
    std::array<float, 10> values = {
        3, 17, 4, 0, 5, 1, -10, -20, 8, 2
    };

    for (int i = -20; i < 20; i++) {
        std::cout << lerp_signal(values.begin(), values.end(), i * 0.5f) << ", ";
    }
    std::cout << '\n';

    std::array<Point, 3> points = {
        Point{3, 5},
        Point{-3, -4},
        Point{15, 7}
    };

    for (int i = -6; i < 6; i++) {
        std::cout << lerp_signal(points.begin(), points.end(), i * 0.5f) << ", ";
    }
    std::cout << '\n';
}
