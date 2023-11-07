package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Comparator;

public class GymProximityComparator implements Comparator<Object> {

    private final Address gymAddress;

    public GymProximityComparator(Address gymAddress) {
        this.gymAddress = gymAddress;
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return 0;
        }

        if (!(obj1 instanceof GymMember)) {
            return -1;
        }

        if (!(obj2 instanceof GymMember)) {
            return 1;
        }

        GymMember member1 = (GymMember) obj1;
        GymMember member2 = (GymMember) obj2;

        return Double.compare(
            member1.getAddress().getDistanceTo(gymAddress),
            member2.getAddress().getDistanceTo(gymAddress)
        );
    }

}
