package com.dustinbayer.macrohomelauncher;

import android.widget.LinearLayout;

import com.eftimoff.patternview.cells.Cell;

import java.util.List;
import java.util.Map;

/**
 * Created by dusti on 11/30/2017.
 */

public class MacroTools {
    public final static int MACRO_SIZE = 3;

    public static String cellsToKey(List<Cell> cells) {
        String key = "";
        for (int i = 0; i < MACRO_SIZE; i++)
            key = key + ":" + cells.get(i).getId();

        return key;
    }

    public static int getButtonId(int num) {
        switch (num) {
            case 0:
                return R.id.button_1;
            case 1:
                return R.id.button_2;
            case 2:
                return R.id.button_3;
            case 3:
                return R.id.button_4;
            case 4:
                return R.id.button_5;
            case 5:
                return R.id.button_6;
            case 6:
                return R.id.button_7;
            case 7:
                return R.id.button_8;
            case 8:
                return R.id.button_9;
        }
        return 0;
    }

    public static int getCellButtonId(Cell cell) {
        switch (cell.getId()) {
            case "000-000":
                return R.id.button_1;
            case "000-001":
                return R.id.button_2;
            case "000-002":
                return R.id.button_3;
            case "001-000":
                return R.id.button_4;
            case "001-001":
                return R.id.button_5;
            case "001-002":
                return R.id.button_6;
            case "002-000":
                return R.id.button_7;
            case "002-001":
                return R.id.button_8;
            case "002-002":
                return R.id.button_9;
        }
        return 0;
    }

    public static int getCellLayoutId(Cell cell) {
        switch (cell.getId()) {
            case "000-000":
                return R.id.macro_1;
            case "000-001":
                return R.id.macro_2;
            case "000-002":
                return R.id.macro_3;
            case "001-000":
                return R.id.macro_4;
            case "001-001":
                return R.id.macro_5;
            case "001-002":
                return R.id.macro_6;
            case "002-000":
                return R.id.macro_7;
            case "002-001":
                return R.id.macro_8;
            case "002-002":
                return R.id.macro_9;
        }
        return 0;
    }

    public static String[] getMacroKeyArray(String macro) {
        return macro.split(":");
    }

    public static int getKeyLayoutId(String key) {
        switch (key) {
            case "000-000":
                return R.id.macro_1;
            case "000-001":
                return R.id.macro_2;
            case "000-002":
                return R.id.macro_3;
            case "001-000":
                return R.id.macro_4;
            case "001-001":
                return R.id.macro_5;
            case "001-002":
                return R.id.macro_6;
            case "002-000":
                return R.id.macro_7;
            case "002-001":
                return R.id.macro_8;
            case "002-002":
                return R.id.macro_9;
        }
        return 0;
    }
}
