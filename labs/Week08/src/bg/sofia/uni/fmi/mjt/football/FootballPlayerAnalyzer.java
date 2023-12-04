package bg.sofia.uni.fmi.mjt.football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.util.Comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

public class FootballPlayerAnalyzer {

    private final List<String> headers;

    private final List<Player> players;

    public FootballPlayerAnalyzer(Reader reader) {
        players = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                String[] headerTokens = line.split("\\Q;\\E");
                headers = List.of(headerTokens);
            } else {
                headers = new ArrayList<>();
            }

            while ((line = bufferedReader.readLine()) != null) {
                players.add(Player.of(line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred with reading from reader.", e);
        }
    }

    public List<Player> getAllPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Set<String> getAllNationalities() {
        return players
            .stream()
            .map(Player::nationality)
            .collect(Collectors.toUnmodifiableSet());
    }

    public Player getHighestPaidPlayerByNationality(String nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Value of nationality should not be null.");
        }

        return players
            .stream()
            .filter(player -> Objects.equals(player.nationality(), nationality))
            .max(Comparator.comparingLong(Player::wageEuro))
            .orElseThrow(() -> new NoSuchElementException("No player with provided nationality."));
    }

    public Map<Position, Set<Player>> groupByPosition() {
        return players
            .stream()
            .flatMap(player -> player.positions().stream().map(position -> Map.entry(position, player)))
            .collect(
                Collectors.groupingBy(Map.Entry::getKey,
                    Collectors.mapping(Map.Entry::getValue, Collectors.toSet()))
            );
    }

    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if (position == null) {
            throw new IllegalArgumentException("Value of position should not be null.");
        }

        if (budget < 0) {
            throw new IllegalArgumentException("Value of budget should not be negative.");
        }

        return players
            .stream()
            .filter(player -> player.positions().contains(position) && player.valueEuro() <= budget)
            .map(player -> {
                    double result = player.overallRating() + player.potential();
                    result /= player.age();

                    return Map.entry(result, player);
            }
            ).max(Map.Entry.<Double, Player>comparingByKey()).map(Map.Entry::getValue);
    }

    public Set<Player> getSimilarPlayers(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Value of player should not be null.");
        }

        return players
            .stream()
            .filter(current -> {
                if (current.positions()
                    .stream()
                    .noneMatch(player.positions()::contains)
                ) {
                    return false;
                }

                if (current.preferredFoot() != player.preferredFoot()) {
                    return false;
                }

                if (Math.abs(current.overallRating() - player.overallRating()) > TokenIndex.INDEX_THREE.getIndex()) {
                    return false;
                }

                return true;
            })
            .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Value of keyword should not be null.");
        }

        return players
            .stream()
            .filter(player -> player.fullName().contains(keyword))
            .collect(Collectors.toUnmodifiableSet());
    }

}
