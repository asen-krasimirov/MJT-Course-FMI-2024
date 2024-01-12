package bg.sofia.uni.fmi.mjt.space.rocket;

import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_ONE;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_THREE;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_TWO;
import static bg.sofia.uni.fmi.mjt.space.utils.TokenIndex.INDEX_ZERO;

import java.util.Objects;
import java.util.Optional;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {
    public static Rocket of(String rocketData) {
        String[] tokens = rocketData.split("\\Q,\\E", -1);

        String id = tokens[INDEX_ZERO.getIndex()];
        String name = tokens[INDEX_ONE.getIndex()];

        String wikiInfo = tokens[INDEX_TWO.getIndex()];
        Optional<String> wiki;
        if (!wikiInfo.isEmpty()) {
            wiki = Optional.of(wikiInfo);
        } else {
            wiki = Optional.empty();
        }

        String heightToParse = tokens[INDEX_THREE.getIndex()];
        Optional<Double> height;
        if (!heightToParse.isEmpty()) {
            height = Optional.of(Double.valueOf(heightToParse.substring(0, heightToParse.length() - 1)));
        } else {
            height = Optional.empty();
        }

        return new Rocket(id, name, wiki, height);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Rocket r)) {
            return false;
        }

        return id().equals(r.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
