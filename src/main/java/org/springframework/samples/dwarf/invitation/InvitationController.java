package org.springframework.samples.dwarf.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/invite")
@Tag(name = "Invitation", description = "Invitation sent by players")
@SecurityRequirement(name = "bearerAuth")
public class InvitationController {
    private final InvitationService is;

    @Autowired
    public InvitationController(InvitationService is){
        this.is = is;
    }

    
}
