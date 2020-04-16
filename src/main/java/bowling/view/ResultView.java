package bowling.view;

import bowling.domain.frame.Frame;
import bowling.domain.frame.Frames;
import bowling.domain.point.Ordinal;
import bowling.domain.result.GameResult;
import bowling.domain.result.GameResults;

import java.util.stream.IntStream;

import static bowling.domain.frame.FrameResult.*;
import static bowling.domain.point.Ordinal.*;

public class ResultView {
    private static final String BLANK_ONE = " ";
    private static final String BLANK_TWO = "  ";
    private static final String BLANK_THREE = "   ";
    private static final String BLANK_FOUR = "    ";
    private static final String BLANK_FIVE = "     ";
    private static final String BLANK_SIX = "      ";
    private static final String BLOCK_BORDER = "|";
    private static final String LABEL_NAME = "NAME";
    private static final String SYMBOL_STRIKE = "X";
    private static final String SYMBOL_GUTTER = "-";
    private static final String DELIMITER_SPARE = "/";
    private static final int FRAME_ID_FIRST = 1;
    private static final int FRAME_ID_FINAL = 10;
    private static final int MIN_NUMBER_FOR_THREE_DIGITS = 100;
    private static final int MIN_NUMBER_FOR_TWO_DIGITS = 10;
    private static final int OFFSET = -1;
    private static final int OFFSET_DOUBLE = -2;

    public static void print(GameResults gameResults) {
        printLineSeparator();
        printResult(gameResults.getFirstResult(), gameResults.getSecondResult());
    }

    private static void printResult(GameResult gameResult1, GameResult gameResult2) {
        IntStream.rangeClosed(FRAME_ID_FIRST, FRAME_ID_FINAL)
                .forEach(frameId -> printFrameResultSoFar(gameResult1, gameResult2, frameId));
    }

    private static void printFrameResultSoFar(GameResult gameResult1, GameResult gameResult2, int frameId) {
        if (frameId != FRAME_ID_FINAL) {
            printFrameResult(FIRST, gameResult1, gameResult2, frameId);
            printFrameResult(SECOND, gameResult1, gameResult2, frameId);
        }

        if (frameId == FRAME_ID_FINAL) {
            printFrameResult(FIRST, gameResult1, gameResult2, frameId);
            printFrameResult(SECOND, gameResult1, gameResult2, frameId);
            printFrameResult(THIRD, gameResult1, gameResult2, frameId);
            printFrameResult(FOURTH, gameResult1, gameResult2, frameId);
        }
    }

    private static void printFrameResult(Ordinal ordinal, GameResult gameResult1, GameResult gameResult2,
                                         int frameId) {
        if (gameResult1.getFrameByFrameId(frameId).containsOrdinal(ordinal)) {
            print(gameResult1.getName() + "'s turn");
            print(" : ");
            println(gameResult1.getFrameByFrameId(frameId).getPointAtOrdinal(ordinal));
            printPlayInformation();

            printForOnePlayer(ordinal, gameResult1, gameResult1.getFrameByFrameId(frameId));
            printLineSeparator();

            if (frameId == FRAME_ID_FIRST && FIRST.equals(ordinal)) {
                printName(gameResult2.getPlayerName().getName());
            }

            if (frameId == FRAME_ID_FIRST && SECOND.equals(ordinal)) {
                printForOnePlayer(FIRST, gameResult2, gameResult2.getFrameByFrameId(frameId));
            }

            if (frameId != FRAME_ID_FIRST && FIRST.equals(ordinal)) {
                printForOnePlayer(SECOND, gameResult2, gameResult2.getFrameByFrameId(frameId + OFFSET));
            }

            if (frameId != FRAME_ID_FIRST && SECOND.equals(ordinal)) {
                printForOnePlayer(FIRST, gameResult2, gameResult2.getFrameByFrameId(frameId));
            }

            if (frameId == FRAME_ID_FINAL
                    && THIRD.equals(ordinal)
                    && gameResult2.getFrameByFrameId(frameId).containsOrdinal(THIRD)) {
                printForOnePlayer(FIRST, gameResult2, gameResult2.getFrameByFrameId(frameId));
            }

            if (frameId == FRAME_ID_FINAL
                    && THIRD.equals(ordinal)
                    && !gameResult2.getFrameByFrameId(frameId).containsOrdinal(THIRD)) {
                printForOnePlayer(SECOND, gameResult2, gameResult2.getFrameByFrameId(frameId));
            }

            if (frameId == FRAME_ID_FINAL && FOURTH.equals(ordinal)) {
                printForOnePlayer(THIRD, gameResult2, gameResult2.getFrameByFrameId(frameId));
            }

            printThreeLineSeparators();
        }

        if (gameResult2.getFrameByFrameId(frameId).containsOrdinal(ordinal)
                && gameResult1.getFrameByFrameId(frameId).containsOrdinal(ordinal)) {
            print(gameResult2.getName() + "'s turn");
            print(" : ");
            println(gameResult2.getFrameByFrameId(frameId).getPointAtOrdinal(ordinal));
            printPlayInformation();

            printForOnePlayer(ordinal, gameResult1, gameResult1.getFrameByFrameId(frameId));
            printLineSeparator();

            printForOnePlayer(ordinal, gameResult2, gameResult2.getFrameByFrameId(frameId));
            printThreeLineSeparators();
        }


        if (!gameResult2.getFrameByFrameId(frameId).containsOrdinal(ordinal)
                && gameResult1.getFrameByFrameId(frameId).containsOrdinal(ordinal)) {
            print(gameResult2.getName() + "'s turn");
            print(" : ");
            System.out.println("-");
            printPlayInformation();

            printForOnePlayer(ordinal, gameResult1, gameResult1.getFrameByFrameId(frameId));
            printLineSeparator();

            printForOnePlayer(SECOND, gameResult2, gameResult2.getFrameByFrameId(frameId));
            printThreeLineSeparators();
        }
    }

    private static void printForOnePlayer(Ordinal ordinal, GameResult gameResult, Frame frame) {
        printName(gameResult.getName());

        if (SECOND.equals(ordinal)
                && gameResult.getFrameByFrameId(frame.getFrameId()).isResult(STRIKE)
                && frame.getFrameId() != FRAME_ID_FIRST) {
            gameResult.getFrames().getFrames()
                    .subList(0, frame.getFrameId())
                    .forEach(frame1 -> printFrame(frame1));
        } else {
            gameResult.getFrames().getFrames()
                    .subList(0, frame.getFrameId() + OFFSET)
                    .forEach(frame1 -> printFrame(frame1));
        }

        printFrameByOrdinal(frame, ordinal);
        printLineSeparator();

        if (frame.getFrameId() != FRAME_ID_FIRST) {
            printFrameScoreSoFar(gameResult.getFrames(), frame, ordinal);
        }
        printCurrentFrameScore(gameResult.getFrames(), frame, ordinal);
    }

    private static void printFrameScoreSoFar(Frames frames, Frame frame, Ordinal ordinal) {
        printName(BLANK_THREE);

        if (!frames.getPreviousFrame(frame).isResult(STRIKE) && !frame.isFirstFrame()) {
            frames.getFrames()
                    .subList(0, frame.getFrameId() + OFFSET)
                    .forEach(frame1 -> printFrameScore(frames, frame1));
        }

        if (frames.getPreviousFrame(frame).isResult(STRIKE) && FIRST.equals(ordinal)) {
            frames.getFrames()
                    .subList(0, frame.getFrameId() + (OFFSET_DOUBLE))
                    .forEach(frame1 -> printFrameScore(frames, frame1));
        }

        if (frames.getPreviousFrame(frame).isResult(STRIKE) && !FIRST.equals(ordinal)) {
            frames.getFrames()
                    .subList(0, frame.getFrameId() + OFFSET)
                    .forEach(frame1 -> printFrameScore(frames, frame1));
        }
    }

    private static void printFrameScore(Frames frames, Frame frame) {
        print(BLANK_FIVE);

        print(frames.getTotalPointUntil(frame));

        printFormatting(frames, frame);

        printBlockBorder();
    }

    private static void printCurrentFrameScore(Frames frames, Frame frame, Ordinal ordinal) {
        print(BLANK_FOUR);

        if (!frame.isFinalFrame()) {
            printNormalFrameScore(frames, frame, ordinal);
        }

        if (frame.isFinalFrame()) {
            printFinalFrameScore(frames, frame, ordinal);
        }

        printFormatting(frames, frame);
        printBlockBorder();
    }

    private static void printNormalFrameScore(Frames frames, Frame frame, Ordinal ordinal) {
        if (frame.isResult(STRIKE) || frame.isResult(SPARE) || FIRST.equals(ordinal)) {
            print(BLANK_THREE);
        }

        if (SECOND.equals(ordinal) && frame.isGutterOrMiss() && !frame.isFirstFrame()) {
            print(frames.getTotalPointUntil(frame));
            print(BLANK_ONE);
        }

        if (SECOND.equals(ordinal) && frame.isFirstFrame() && !frame.isResult(SPARE)) {
            print(BLANK_SIX);
            print(BLOCK_BORDER);
            print(BLANK_FOUR);
            print(frames.getTotalPointUntil(frame));
            print(BLANK_ONE);
        }
    }

    private static void printFinalFrameScore(Frames frames, Frame frame, Ordinal ordinal) {
        if (FIRST.equals(ordinal) || SECOND.equals(ordinal) && frame.containsOrdinal(THIRD)) {
            print(BLANK_TWO);
        }

        if (SECOND.equals(ordinal) && !frame.containsOrdinal(THIRD)) {
            print(frames.getTotalPointUntil(frame));
        }

        if ((THIRD.equals(ordinal) && frame.isResult(SPARE))) {
            print(frames.getTotalPointUntil(frame));
        }

        if (FOURTH.equals(ordinal) && frame.isResult(STRIKE)) {
            print(frames.getTotalPointUntil(frame));
        }
    }

    private static void printFormatting(Frames frames, Frame frame) {
        if (frames.getTotalPointUntil(frame) >= MIN_NUMBER_FOR_THREE_DIGITS) {
        }

        if (frames.getTotalPointUntil(frame) >= MIN_NUMBER_FOR_TWO_DIGITS) {
            print(BLANK_THREE);
        }

        if (frames.getTotalPointUntil(frame) < MIN_NUMBER_FOR_TWO_DIGITS) {
            print(BLANK_FOUR);
        }
    }

    private static void printFrame(Frame frame) {
        if (frame.isResult(STRIKE)) {
            printStrikeByOrdinal(frame, FIRST);
        }

        if (frame.isResult(SPARE)) {
            printSpareByOrdinal(frame, SECOND);
        }

        if (frame.isResult(MISS)) {
            printMissByOrdinal(frame, SECOND);
        }

        if (frame.isResult(GUTTER)) {
            printGutter();
        }
    }

    private static void printFrameByOrdinal(Frame frame, Ordinal ordinal) {
        if (frame.isResult(STRIKE)) {
            printStrikeByOrdinal(frame, ordinal);
        }

        if (frame.isResult(SPARE)) {
            printSpareByOrdinal(frame, ordinal);
        }

        if (frame.isResult(MISS)) {
            printMissByOrdinal(frame, ordinal);
        }

        if (frame.isResult(GUTTER)) {
            printGutter();
        }
    }

    private static void printStrikeByOrdinal(Frame frame, Ordinal ordinal) {
        if (FIRST.equals(ordinal)) {
            printFrameFirstWhenStrike();
        }

        if (THIRD.equals(ordinal)) {
            printFrameThirdWhenStrike(frame);
        }

        if (FOURTH.equals(ordinal)) {
            printFrameFourthWhenStrike(frame);
        }
    }

    private static void printFrameFirstWhenStrike() {
        print(BLANK_FIVE);
        print(SYMBOL_STRIKE);
        print(BLANK_FOUR);
        printBlockBorder();
    }

    private static void printFrameThirdWhenStrike(Frame frame) {
        print(BLANK_FIVE);
        print(SYMBOL_STRIKE);
        print(BLOCK_BORDER);
        print(frame.getThirdPoint());
    }

    private static void printFrameFourthWhenStrike(Frame frame) {
        printFrameThirdWhenStrike(frame);
        print(BLOCK_BORDER);
        print(frame.getFourthPoint());
        print(BLANK_ONE);
        printBlockBorder();
    }

    private static void printSpareByOrdinal(Frame frame, Ordinal ordinal) {
        if (FIRST.equals(ordinal)) {
            printFrameFirstWhenSpare(frame);
        }

        if (SECOND.equals(ordinal)) {
            printFrameSecondWhenSpare(frame);
        }

        if (THIRD.equals(ordinal)) {
            printFrameThirdWhenSpare(frame);
        }
    }

    private static void printFrameFirstWhenSpare(Frame frame) {
        print(BLANK_FOUR);
        print(frame.getFirstPoint());

        print(BLANK_TWO);
        print(BLANK_THREE);
        printBlockBorder();
    }

    private static void printFrameSecondWhenSpare(Frame frame) {
        print(BLANK_FOUR);
        print(frame.getFirstPoint());

        print(BLOCK_BORDER);
        print(DELIMITER_SPARE);

        print(BLANK_THREE);
        printBlockBorder();
    }

    private static void printFrameThirdWhenSpare(Frame frame) {
        print(BLANK_FOUR);
        print(frame.getFirstPoint());

        print(BLOCK_BORDER);
        print(DELIMITER_SPARE);

        print(BLOCK_BORDER);
        print(frame.getThirdPoint());

        print(BLANK_ONE);
        printBlockBorder();
    }

    private static void printMissByOrdinal(Frame frame, Ordinal ordinal) {
        if (FIRST.equals(ordinal)) {
            printFrameFirstWhenMiss(frame);
        }

        if (SECOND.equals(ordinal)) {
            printFrameSecondWhenMiss(frame);
        }
    }

    private static void printFrameFirstWhenMiss(Frame frame) {
        print(BLANK_FOUR);
        print(frame.getFirstPoint());
        print(BLANK_FIVE);
        printBlockBorder();
    }

    private static void printFrameSecondWhenMiss(Frame frame) {
        print(BLANK_FOUR);
        print(frame.getFirstPoint());
        print(BLOCK_BORDER);
        print(frame.getSecondPoint());
        print(BLANK_THREE);
        printBlockBorder();
    }

    private static void printGutter() {
        print(BLANK_FIVE);
        print(SYMBOL_GUTTER);
        print(BLANK_FOUR);
        printBlockBorder();
    }

    private static void printPlayInformation() {
        printNameColumn(LABEL_NAME);
        IntStream.range(1, 11)
                .forEach(it -> printOneBlockWith(convertFrameNumberToString(it)));
        printLineSeparator();
    }

    private static void printNameColumn(String name) {
        printBlockBorder();
        printOneBlockWith(name);
    }

    private static void printName(String name) {
        printBlockBorder();
        print(BLANK_ONE);
        printOneBlockWith(name);
    }

    private static void printOneBlockWith(String message) {
        print(BLANK_THREE);
        print(message);
        print(BLANK_THREE);
        printBlockBorder();
    }

    private static String convertFrameNumberToString(int number) {
        String stringNumber
                = (number >= MIN_NUMBER_FOR_TWO_DIGITS)
                ? String.valueOf(BLANK_ONE + number + BLANK_ONE)
                : " 0" + number + BLANK_ONE;
        return stringNumber;
    }

    private static void printBlockBorder() {
        print(BLOCK_BORDER);
    }

    private static void printThreeLineSeparators() {
        IntStream.range(0, 3)
                .forEach(IntConsumer -> printLineSeparator());
    }

    private static void printLineSeparator() {
        System.out.println();
    }

    private static void print(String message) {
        System.out.print(message);
    }

    private static void print(int number) {
        System.out.print(number);
    }

    private static void println(int message) {
        System.out.println(message);
    }
}