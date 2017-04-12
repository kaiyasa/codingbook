
// reference:
// http://stackoverflow.com/questions/29512963/shorthand-for-for-loop-syntactic-sugar-in-c11


// boost::adaptors::indexed
// http://www.boost.org/doc/libs/master/libs/range/doc/html/range/reference/adaptors/reference/indexed.html
#include <boost/range/adaptor/indexed.hpp>

// boost::irange
// http://www.boost.org/doc/libs/master/libs/range/doc/html/range/reference/ranges/irange.html
#include <boost/range/irange.hpp>

#include <iostream>
#include <vector>

int main()
{
    std::vector<int> input{11, 22, 33, 44, 55};
    std::cout << "boost::adaptors::indexed" << '\n';
    for (const auto & element : input | boost::adaptors::indexed())
    {
        std::cout << "Value = " << element.value()
                  << " Index = " << element.index()
                  << '\n';
    }

    endl(std::cout);

    std::cout << "boost::irange" << '\n';
    for (const auto & element : boost::irange(0, 5) | boost::adaptors::indexed(100))
    {
        std::cout << "Value = " << element.value()
                  << " Index = " << element.index()
                  << '\n';
    }

    return 0;
}
