

for i in xrange(1, 3):
  print 'loop one: %d' % (i)
else:
  print "loop one - optional else"
print "loop one - end"

print ""

for i in xrange(1, 9):
  print 'loop two: %d' % (i)
  if i == 2:
    break
else:
  print "loop two - optional else"
print "loop two - end"

print ""

def my_range(start, end, step):
  while start < end:
    yield start
    start += step

for i in my_range(1, 9, 2):
  print "loop three: %d" % (i)
print "loop three - end"

class Silly:
  def __init__(self):
    self.value = 1

  def __iter__(self):
    class MyIterator:
      def __init__(self, start):
        self.value = start

      def next(self):
        if self.value == 3:
            raise StopIteration

        value = self.value
        self.value += 1
        return value

    return MyIterator(self.value)

print ""

item = Silly()
for i in item:
  print "loop four: %d" % (i)
print "loop four - end"
assert item.value == 1
