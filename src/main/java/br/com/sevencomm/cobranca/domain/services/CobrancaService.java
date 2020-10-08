package br.com.sevencomm.cobranca.domain.services;

import br.com.sevencomm.cobranca.application.configs.exception.ObjectNotFoundException;
import br.com.sevencomm.cobranca.data.repositories.AreaRepository;
import br.com.sevencomm.cobranca.data.repositories.CobrancaRepository;
import br.com.sevencomm.cobranca.data.repositories.UserRepository;
import br.com.sevencomm.cobranca.data.repositories.UsuarioAreasRepository;
import br.com.sevencomm.cobranca.domain.interfaces.ICobrancaService;
import br.com.sevencomm.cobranca.domain.models.Cobranca;
import br.com.sevencomm.cobranca.domain.models.Usuario;
import br.com.sevencomm.cobranca.domain.models.UsuarioAreas;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CobrancaService implements ICobrancaService {

    private final UserRepository usuarioRepository;
    private final CobrancaRepository cobrancaInternaRepository;
    private final AreaRepository areaRepository;
    private final UsuarioAreasRepository userAreaRepository;

    public CobrancaService(CobrancaRepository cobrancaInternaRepository, UserRepository usuarioRepository, AreaRepository areaRepository, UsuarioAreasRepository userAreaRepository) {
        this.cobrancaInternaRepository = cobrancaInternaRepository;
        this.usuarioRepository = usuarioRepository;
        this.areaRepository = areaRepository;
        this.userAreaRepository = userAreaRepository;
    }

    public Cobranca getCobrancaById(Integer cobrancaId) {
        Usuario usuario = getCurrentUser();

        Optional<Cobranca> fndCobranca = cobrancaInternaRepository.findById(cobrancaId);

        if (!fndCobranca.isPresent())
            throw new ObjectNotFoundException("Cobranca not found");

        if (!userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), fndCobranca.get().getBeneficiarioAreaId()).isPresent() &&
                !userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), fndCobranca.get().getPagadorAreaId()).isPresent())
            throw new IllegalArgumentException("Usuario not allowed");

        //mesmo metodo do que o de cima, mas deixar para legado
        //if (!(fndCobranca.get().getBeneficiarioAreaId().equals(usuario.getAreaId())) && !(fndCobranca.get().getPagadorAreaId().equals(usuario.getAreaId())))
        // throw new IllegalArgumentException("Usuario not allowed");

        return fndCobranca.get();
    }

    public void aprovarCobranca(Integer id) {
        Usuario usuario = getCurrentUser();

        Optional<Cobranca> fndCobranca = cobrancaInternaRepository.findById(id);

        if (!fndCobranca.isPresent())
            throw new IllegalArgumentException("Cobranca not found");

        if (!userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), fndCobranca.get().getBeneficiarioAreaId()).isPresent())
            throw new IllegalArgumentException("Usuario not allowed");

        //mesmo metodo do que o de cima, mas deixar para legado
        //if (!(fndCobranca.get().getBeneficiarioAreaId().equals(usuario.getAreaId())))
        //throw new IllegalArgumentException("Usuario not allowed");

        Cobranca cobranca = fndCobranca.get();

        if (cobranca.getStatusId()!=1)
            throw new IllegalArgumentException("Already approved");

        cobranca.setStatusId(2);

        cobrancaInternaRepository.save(cobranca);
    }

    public void recusarCobranca(Integer id) {
        Usuario usuario = getCurrentUser();

        Optional<Cobranca> fndCobranca = cobrancaInternaRepository.findById(id);

        if (!fndCobranca.isPresent())
            throw new IllegalArgumentException("Cobranca not found");

        if (!userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), fndCobranca.get().getBeneficiarioAreaId()).isPresent())
            throw new IllegalArgumentException("Usuario not allowed");

        //mesmo metodo do que o de cima, mas deixar para legado
        //if (!(fndCobranca.get().getBeneficiarioAreaId().equals(usuario.getAreaId())))
        //throw new IllegalArgumentException("Usuario not allowed");

        Cobranca cobranca = fndCobranca.get();

        if (cobranca.getStatusId()!=1)
            throw new IllegalArgumentException("Already recused");

        cobranca.setStatusId(3);

        cobrancaInternaRepository.save(cobranca);
    }

    public List<Cobranca> listCobrancas() {
        return cobrancaInternaRepository.findAll();
    }

    public List<Cobranca> listCobrancasByArea(Integer areaId) {
        if(!areaRepository.findById(areaId).isPresent())
            throw new IllegalArgumentException("Area not found");

        List<Cobranca> cobrancaList = new ArrayList<>();
        List<UsuarioAreas> usuarioAreasList = userAreaRepository.findAllByAreaId(areaId);

        for (UsuarioAreas u : usuarioAreasList) {
            List<Cobranca> cobrancas = cobrancaInternaRepository.findAllByPagadorAreaIdOrBeneficiarioAreaId(u.getAreaId(), u.getAreaId());
            if (!cobrancas.isEmpty()) cobrancaList.addAll(cobrancas);
        }

        return cobrancaList;
    }

    public List<Cobranca> listCurrentUserCobrancas() {
        Usuario usuario = getCurrentUser();
        List<Cobranca> cobrancaList = new ArrayList<>();
        List<UsuarioAreas> usuarioAreasList = userAreaRepository.findAllByUsuarioId(usuario.getId());

        for (UsuarioAreas u : usuarioAreasList) {
            List<Cobranca> cobrancas = cobrancaInternaRepository.findAllByPagadorAreaIdOrBeneficiarioAreaId(u.getAreaId(), u.getAreaId());
            if (!cobrancas.isEmpty()) cobrancaList.addAll(cobrancas);
        }

        return cobrancaList;
    }

    public List<Cobranca> listCobrancasEnviadas() {
        Usuario usuario = getCurrentUser();
        List<Cobranca> cobrancaList = new ArrayList<>();
        List<UsuarioAreas> usuarioAreasList = userAreaRepository.findAllByUsuarioId(usuario.getId());

        for (UsuarioAreas u : usuarioAreasList) {
            List<Cobranca> cobrancas = cobrancaInternaRepository.findAllByPagadorAreaId(u.getAreaId());
            if (!cobrancas.isEmpty()) cobrancaList.addAll(cobrancas);
        }

        return cobrancaList;
    }

    public List<Cobranca> listCobrancasRecebidas() {
        Usuario usuario = getCurrentUser();
        List<Cobranca> cobrancaList = new ArrayList<>();
        List<UsuarioAreas> usuarioAreasList = userAreaRepository.findAllByUsuarioId(usuario.getId());

        for (UsuarioAreas u : usuarioAreasList) {
            List<Cobranca> cobrancas = cobrancaInternaRepository.findAllByBeneficiarioAreaId(u.getAreaId());
            if (!cobrancas.isEmpty()) cobrancaList.addAll(cobrancas);
        }

        return cobrancaList;
    }

    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Optional<Usuario> optUsuario = usuarioRepository.findById(usuario.getId());

        if (!optUsuario.isPresent())
            throw new IllegalArgumentException("Illegal api access");

        return optUsuario.get();
    }

    public Cobranca updateCobranca(Cobranca cobrancaInterna, Integer id) {
        Usuario usuario = getCurrentUser();

        Optional<Cobranca> optAux = cobrancaInternaRepository.findById(id);

        if (!optAux.isPresent())
            throw new IllegalArgumentException("Cobrança not found");

        Cobranca ciAux = optAux.get();

        if (!userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), ciAux.getPagadorAreaId()).isPresent())
            throw new IllegalArgumentException("Precisa pertencer a area para atualizar a cobrança nesta area");

        ciAux.setPagadorAreaId(cobrancaInterna.getPagadorAreaId());
        ciAux.setStatusId(1); //caso o requisitor altere, o status volta pra 1
        ciAux.setBeneficiarioAreaId(cobrancaInterna.getBeneficiarioAreaId());
        ciAux.setDatahora(cobrancaInterna.getDatahora());
        ciAux.setDescricao(cobrancaInterna.getDescricao());
        ciAux.setValor(cobrancaInterna.getValor());

        if (ciAux.getValor().isNaN())
            throw new IllegalArgumentException("Valor inválido");

        if (!areaRepository.findById(ciAux.getPagadorAreaId()).isPresent())
            throw new IllegalArgumentException("Pagador area not found");

        if (ciAux.getDescricao().length() <= 0)
            throw new IllegalArgumentException("Descrição cannot be null");

        if (!areaRepository.findById(ciAux.getBeneficiarioAreaId()).isPresent())
            throw new IllegalArgumentException("Beneficiario area not found");

        return cobrancaInternaRepository.save(ciAux);
    }

    public void deleteCobranca(Integer id) {
        Usuario usuario = getCurrentUser();

        Optional<Cobranca> optCobranca = cobrancaInternaRepository.findById(id);

        if (!optCobranca.isPresent()) return;

        Cobranca cobranca = optCobranca.get();

        if (!userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), cobranca.getPagadorAreaId()).isPresent())
            throw new IllegalArgumentException("Precisa pertencer a area para apagar a cobrança");

        cobrancaInternaRepository.deleteById(id);
    }

    public Cobranca insertCobranca(Cobranca cobrancaInterna) {
        Usuario usuario = getCurrentUser();

        if (!userAreaRepository.findFirstByUsuarioIdAndAreaId(usuario.getId(), cobrancaInterna.getPagadorAreaId()).isPresent())
            throw new IllegalArgumentException("Precisa pertencer a area para inserir a cobrança nesta area");

        if (cobrancaInterna.getDescricao().length() <= 0)
            throw new IllegalArgumentException("Descrição cannot be null");

        if (cobrancaInterna.getValor() <= 0)
            throw new IllegalArgumentException("Valor cannot be 0 or less");

        if (cobrancaInterna.getPagadorAreaId().equals(cobrancaInterna.getBeneficiarioAreaId()))
            throw new IllegalArgumentException("Pagador e beneficiario pertencem a mesma area");

        if (!areaRepository.findById(cobrancaInterna.getPagadorAreaId()).isPresent())
            throw new IllegalArgumentException("Pagador area not found");

        if (!areaRepository.findById(cobrancaInterna.getBeneficiarioAreaId()).isPresent())
            throw new IllegalArgumentException("Beneficiario area not found");

        cobrancaInterna.setStatusId(1);
        cobrancaInterna.setPagadorAreaId(cobrancaInterna.getPagadorAreaId());

        return cobrancaInternaRepository.save(cobrancaInterna);
    }

    public List<Cobranca> listAllByStatus(Integer status) {
        return cobrancaInternaRepository.findAllByStatusId(status);
    }
}
