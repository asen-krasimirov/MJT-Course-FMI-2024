package bg.sofia.uni.fmi.mjt.response;

import java.util.List;

public record ResponseHolder(
    int from,
    int to,
    int count,
    Links _links,
    List<Hit> hits
) {

    public record Links(NextPage next) {

        public record NextPage(String href, String title) {
        }

    }

    public record Hit(RecipeInfoHolder recipe, Links _links) {

        public record RecipeInfoHolder(
            String label,
            List<String> dietLabels,
            List<String> healthLabels,
            List<String> ingredientLines,
            double totalWeight,
            List<String> cuisineType,
            List<String> mealType,
            List<String> dishType
        ) {
        }

        public record Links(Self self) {

            public record Self(String title, String href) {
            }

        }

    }
}
