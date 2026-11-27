package cn.jiguang.imui.chatinput.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FileItemComparatorTest {

    @Test
    public void sortsGalleryItemsNewestFirstAcrossMonths() {
        List<FileItem> items = new ArrayList<>();
        long firstDay = 1704067200000L;
        long oneDay = 24 * 60 * 60 * 1000L;

        for (int index = 0; index < 64; index++) {
            items.add(new FileItem("/media/" + index, "image", "0",
                    Long.toString(firstDay + index * oneDay)));
        }
        Collections.shuffle(items, new Random(5));

        Collections.sort(items);

        assertEquals(64, items.size());
        for (int index = 1; index < items.size(); index++) {
            long previousDate = Long.parseLong(items.get(index - 1).getDate());
            long currentDate = Long.parseLong(items.get(index).getDate());
            assertTrue(previousDate >= currentDate);
        }
    }

    @Test
    public void sortsInvalidDatesAfterValidDates() {
        List<FileItem> items = new ArrayList<>();
        items.add(new FileItem("/media/invalid", "image", "0", ""));
        items.add(new FileItem("/media/older", "image", "0", "1000"));
        items.add(new FileItem("/media/null", "image", "0", null));
        items.add(new FileItem("/media/newer", "image", "0", "2000"));

        Collections.sort(items);

        assertEquals("2000", items.get(0).getDate());
        assertEquals("1000", items.get(1).getDate());
        assertEquals("", items.get(2).getDate());
        assertNull(items.get(3).getDate());
    }
}
