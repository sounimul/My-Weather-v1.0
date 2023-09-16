package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import weather.weatherspring.entity.Member;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Optional<Member> findByUid(Long uid) {     // pk 값
        Member member = em.find(Member.class,uid);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findById(String id) {
        List<Member> result = em.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();
        return result.stream().findAny();
    }

    public List<Member> findAll(String avail){
        List<Member> result = em.createQuery("select m from Member m where m.avail = :avail order by m.uid asc",Member.class)
                .setParameter("avail",avail)
                .getResultList();
        return result;
    }

}
