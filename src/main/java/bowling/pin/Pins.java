package bowling.pin;

import bowling.ball.Ball;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static bowling.global.utils.CommonConstant.NUMBER_TWO;

public class Pins {

    private List<Pin> pins;

    public Pins(List<Pin> pins) {
        this.pins = pins;
    }

    public static Pins eachPitchResult(List<Pin> pinList, String pitchPoint, int pitchCount) {
        pinsClear(pinList);
        Pin pin = Pin.eachPitch(pinList, Ball.pitch(pitchPoint, pitchCount));
        pinList.add(pin);
        return new Pins(pinList);
    }

    private static void pinsClear(List<Pin> pins) {
        if (pins.size() == NUMBER_TWO) {
            pins.clear();
        }
    }

    public int size() {
        return pins.size();
    }

    public List<Pin> getPins() {
        return Collections.unmodifiableList(pins);
    }

    public Pin getPinsIndex(int index) {
        return pins.get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pins pins1 = (Pins) o;
        return Objects.equals(pins, pins1.pins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pins);
    }

    @Override
    public String toString() {
        return String.valueOf(pins);
    }
}
