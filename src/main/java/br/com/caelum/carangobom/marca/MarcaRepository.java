package br.com.caelum.carangobom.marca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class MarcaRepository {

    private EntityManager entityManager;

    @Autowired
    public MarcaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void delete(Marca marca) {
        entityManager.remove(marca);
    }

    public Marca save(Marca marca) {
        entityManager.persist(marca);
        return marca;
    }

    public Optional<Marca> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Marca.class, id));
    }

    public List<Marca> findAllByOrderByNome() {
        return entityManager.createQuery("select m from Marca m order by m.nome", Marca.class)
                .getResultList();
    }

    public Optional<Marca> findByName(String nomeMarca) {
        TypedQuery<Marca> query = entityManager.createQuery("select m from Marca m where m.nome = :nomeMarca", Marca.class);
        query = query.setParameter("nomeMarca", nomeMarca);

        return Optional.ofNullable(query
                .getSingleResult());
    }

}
