package com.example.SIGR.services;
import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;
import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.repository.TypeUniteRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import com.example.SIGR.security.SecurityUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeUniteServiceImpl implements TypeUniteService {

    private final TypeUniteRepository typeUniteRepository;
    private final UniteAdministrativeRepository uniteAdministrativeRepository;

    public TypeUniteServiceImpl(
            TypeUniteRepository typeUniteRepository,
            UniteAdministrativeRepository uniteAdministrativeRepository
    ) {
        this.typeUniteRepository         = typeUniteRepository;
        this.uniteAdministrativeRepository = uniteAdministrativeRepository;
    }

    // ================= CREATE =================
    @Override
    public TypeUniteResponse create(TypeUniteRequest request) {

        // ================= DOUBLON CODE =================
        if (typeUniteRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Un type d'unité existe déjà avec le code : " + request.getCode()
            );
        }

        // ================= DOUBLON LIBELLE =================
        if (typeUniteRepository.existsByLibelleIgnoreCase(request.getLibelle())) {
            throw new RuntimeException(
                    "Un type d'unité existe déjà avec le libellé : " + request.getLibelle()
            );
        }

        TypeUnite typeUnite = new TypeUnite();
        typeUnite.setCode(request.getCode());
        typeUnite.setLibelle(request.getLibelle());
        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(SecurityUtils.getCurrentRole());

        return toResponse(typeUniteRepository.save(typeUnite));
    }

    // ================= GET BY CODE =================
    @Override
    public TypeUniteResponse getByCode(String code) {
        TypeUnite typeUnite = typeUniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Type d'unité introuvable (code) : " + code)
                );
        return toResponse(typeUnite);
    }

    // ================= GET ALL =================
    @Override
    public List<TypeUniteResponse> getAll() {
        return typeUniteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public TypeUniteResponse update(String code, TypeUniteRequest request) {

        TypeUnite typeUnite = typeUniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Type d'unité introuvable (code) : " + code)
                );

        // ================= DOUBLON LIBELLE (hors entité courante) =================
        if (request.getLibelle() != null) {
            boolean libelleDejaUtilise = typeUniteRepository
                    .existsByLibelleIgnoreCaseAndCodeNot(request.getLibelle(), code);
            if (libelleDejaUtilise) {
                throw new RuntimeException(
                        "Un autre type d'unité existe déjà avec le libellé : " + request.getLibelle()
                );
            }
            typeUnite.setLibelle(request.getLibelle());
        }

        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(SecurityUtils.getCurrentRole());

        return toResponse(typeUniteRepository.save(typeUnite));
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        TypeUnite typeUnite = typeUniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Type d'unité introuvable (code) : " + code)
                );

        // ================= VERIFICATION ASSOCIATION =================
        if (uniteAdministrativeRepository.existsByTypeUnite(typeUnite)) {
            throw new RuntimeException(
                    "Impossible de supprimer le type d'unité « " + typeUnite.getLibelle()
                            + " » : il est associé à une ou plusieurs unités administratives."
            );
        }

        typeUniteRepository.delete(typeUnite);
    }

    // ================= MAPPER =================
    private TypeUniteResponse toResponse(TypeUnite typeUnite) {
        return new TypeUniteResponse(
                typeUnite.getId(),
                typeUnite.getCode(),
                typeUnite.getLibelle(),
                typeUnite.getDescription(),
                typeUnite.getCreePar()
        );
    }
}