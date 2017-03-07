package transformers;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.models.GlobalAppStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 2/25/17.
 */
@Transactional(readOnly = true)
public class TeamMemberTransformer {

    /**
     * Transform a team member entity to userDTO.
     *
     * Used to create sppoter or only team member.
     *
     * @return all team member information inside a DTO.
     */
    public static UserDTO teamMemberEntityToDto(TeamMemberEntity memberEntity, SppotiEntity sppoti, Integer sppoterStatus) {

        UserDTO userCoverAndAvatar = EntityToDtoTransformer.getUserCoverAndAvatar(memberEntity.getUsers());

        return new UserDTO(memberEntity.getUuid(), memberEntity.getUsers().getFirstName(), memberEntity.getUsers().getLastName(), memberEntity.getUsers().getUsername(),
                userCoverAndAvatar.getCover() != null ? userCoverAndAvatar.getCover() : null,
                userCoverAndAvatar.getAvatar() != null ? userCoverAndAvatar.getAvatar() : null,
                userCoverAndAvatar.getCoverType() != null ? userCoverAndAvatar.getCoverType() : null,
                memberEntity.getAdmin(),
                sppoti != null && sppoti.getUserSppoti().getId() != null && memberEntity.getUsers().getId().equals(sppoti.getUserSppoti().getId()) ? true : null,
                GlobalAppStatus.valueOf(memberEntity.getStatus()).getValue(),
                sppoti != null && sppoti.getUserSppoti().getId() != null ? sppoterStatus : null, memberEntity.getUsers().getUuid(),
                memberEntity.getxPosition() != null ? memberEntity.getxPosition() : null,
                memberEntity.getyPosition() != null ? memberEntity.getyPosition() : null,
                memberEntity.getTeamCaptain() != null ? memberEntity.getTeamCaptain() : null);
    }

}
