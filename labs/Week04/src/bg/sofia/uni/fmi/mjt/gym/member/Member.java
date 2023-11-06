package bg.sofia.uni.fmi.mjt.gym.member;

public class Member {
    private Address address;
    private String name;
    private int age;
    private String personalIdNumber;
    private Gender gender;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Member)) {
            return false;
        }

        Member member = (Member) obj;

        return this.personalIdNumber.equals(member.personalIdNumber);
    }
}
