package ad1.ss16.pa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by gabriel on 29.04.2016.
 */
@RunWith(Parameterized.class)
public class InstanceTest {
    private final File instance;

    public InstanceTest(File instance, String name) throws FileNotFoundException {
        this.instance = instance;
    }

    @Test
    public void testInstance() {
        Tester.main(new String[]{"-d", instance.getAbsolutePath()});
    }

    @Parameterized.Parameters(name="{1}")
    public static Iterable<Object[]> getFiles() {
        File folder = new File(InstanceTest.class.getResource("/instances").getFile());
        return Arrays.stream(folder.listFiles()).map((f) -> {
            return new Object[] {f, f.getName()};
        })::iterator;
    }
}
