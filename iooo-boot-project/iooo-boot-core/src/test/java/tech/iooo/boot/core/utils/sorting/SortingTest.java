package tech.iooo.boot.core.utils.sorting;

import com.google.common.base.Joiner;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2019-03-19 15:27
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class SortingTest {

  private static final Logger logger = LoggerFactory.getLogger(SortingTest.class);

  private static List<Integer> nums;

  @BeforeAll
  static void init() {
    Random random = new Random();
    nums = random.ints(1000, 10, 100000).boxed().collect(Collectors.toList());
  }


  @Test
  void insertSorting() {
    InsertSorting<Integer> integerInsertSorting = new InsertSorting<>();
    log(integerInsertSorting.apply(nums.toArray(new Integer[0])));
  }

  @Test
  void selectionSorting() {
    SelectionSorting<Integer> selectionSorting = new SelectionSorting<>();
    log(selectionSorting.apply(nums.toArray(new Integer[0])));
  }

  @Test
  void shellSorting() {
    ShellSorting<Integer> shellSorting = new ShellSorting<>();
    log(shellSorting.apply(nums.toArray(new Integer[0])));
  }

  @Test
  void heapSorting() {
    HeapSorting<Integer> heapSorting = new HeapSorting<>();
    log(heapSorting.apply(nums.toArray(new Integer[0])));
  }

  @Test
  void mergeSorting() {
    MergeSorting<Integer> mergeSorting = new MergeSorting<>();
    log(mergeSorting.apply(nums.toArray(new Integer[0])));
  }

  @Test
  void quickSorting() {
    QuickSorting<Integer> quickSorting = new QuickSorting<>();
    log(quickSorting.apply(nums.toArray(new Integer[0])));
  }

  private void log(Object[] objects) {
    if (logger.isInfoEnabled()) {
      logger.info("{}", Joiner.on(",").join(objects));
    }
  }
}
