package ttl.larku.dao.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

/**
 * An interface for a custom Repository.  We can use this
 * to completely specify our repository interface
 * @author whynot
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface CustomRepoBase<T, ID extends Serializable> extends Repository<T, ID> {

	//Can either return an Optional
	Optional<T> findById(@Param("id") ID id);

	//Or a null
	@Nullable
	T findNullableById(@Param("id") ID id);
	
	//Or get an EmptyResultDataAccessException
	T findExceptionById(@Param("id") ID id);

	List<T> findAll();
}
