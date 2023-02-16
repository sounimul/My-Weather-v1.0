package weather.weatherspring.repository;

import weather.weatherspring.domain.Member;

public interface MemberRepository {

    Member save(Member member);
}
