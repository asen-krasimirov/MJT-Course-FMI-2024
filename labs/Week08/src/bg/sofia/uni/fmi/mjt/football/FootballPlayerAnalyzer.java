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
import java.util.stream.Stream;

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

    /**
     * Returns an Optional containing the top prospect player in the dataset that can play in the provided position and
     * that can be bought with the provided budget considering the player's value_euro. If no player can be bought with
     * the provided budget then return an empty Optional.
     * <p>
     * The player's prospect is calculated by the following formula: Prospect = (r + p) ÷ a where r is the player's
     * overall rating, p is the player's potential and a is the player's age
     *
     * @param position the position in which the player should be able to play
     * @param budget   the available budget for buying a player
     * @return an Optional containing the top prospect player
     * @throws IllegalArgumentException in case the provided position is null or the provided budget is negative
     */
    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    /**
     * Returns an unmodifiable set of players that are similar to the provided player. Two players are considered
     * similar if: 1. there is at least one position in which both of them can play 2. both players prefer the same foot
     * 3. their overall_rating measures differ by at most 3 (inclusive)
     * If the dataset contains the provided player, the player will be present in the returned result.
     *
     * @param player the player for whom similar players are retrieved. It may or may not be part of the dataset.
     * @return an unmodifiable set of similar players
     * @throws IllegalArgumentException if the provided player is null
     */
    public Set<Player> getSimilarPlayers(Player player) {
        throw new UnsupportedOperationException("Method not yet implemented");
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
