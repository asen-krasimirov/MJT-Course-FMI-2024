package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import static bg.sofia.uni.fmi.mjt.space.utils.DateParser.parseDate;

import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_FIVE;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_FOUR;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_ONE;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_SEVEN;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_SIX;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_THREE;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_TWO;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_ZERO;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public record Mission(String id, String company, String location, LocalDate date, Detail detail,
                      RocketStatus rocketStatus, Optional<Double> cost, MissionStatus missionStatus) {
    public static Mission of(String missionData) {
        String[] tokens = missionData.split("(?!\\B\"[^\"]*),(?![^\"]*\"\\B)");

        String id = tokens[INDEX_ZERO.getIndex()];
        String company = tokens[INDEX_ONE.getIndex()];
        String locationData = tokens[INDEX_TWO.getIndex()];
        String location = locationData.substring(1, locationData.length() - 1);

        String dataToParse = tokens[INDEX_THREE.getIndex()];
        LocalDate localDate = parseDate(dataToParse.substring(1, dataToParse.length() - 1));

        Detail detail = Detail.of(tokens[INDEX_FOUR.getIndex()]);

        RocketStatus rocketStatus = RocketStatus.parseRocket(tokens[INDEX_FIVE.getIndex()]);

        String costToParse = tokens[INDEX_SIX.getIndex()];
        Optional<Double> cost;
        if (!costToParse.isEmpty()) {
            cost = Optional.of(Double.valueOf(costToParse.substring(1, costToParse.length() - 1)));
        } else {
            cost = Optional.empty();
        }

        MissionStatus missionStatus = MissionStatus.parseMission(tokens[INDEX_SEVEN.getIndex()]);

        return new Mission(id, company, location, localDate, detail, rocketStatus, cost, missionStatus);
    }

    public String getCountry() {
        String[] locationTokens = location().split("\\Q, \\E");
        return locationTokens[locationTokens.length - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Mission m)) {
            return false;
        }

        return id().equals(m.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
