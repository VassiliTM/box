package com.nawak;

import com.nawak.dirbox.DirBox;
import com.nawak.dirbox.DirBoxBuilder;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainTest {

    @Before
    public void init() {

    }

    @Test
    public void testMain() throws Exception {

        Path directory = Paths.get("");
        Path destination = Paths.get("");

        DirBoxBuilder dbb = new DirBoxBuilder(directory);

        DirBox dirBox = dbb.keepStructure(true).withPattern("").build();

        dirBox.copy(destination);

        long size = dirBox.folderSize();
        System.out.println("size = " + size);
    }
}
