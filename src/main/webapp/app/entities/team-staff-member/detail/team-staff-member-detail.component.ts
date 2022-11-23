import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeamStaffMember } from '../team-staff-member.model';

@Component({
  selector: 'jhi-team-staff-member-detail',
  templateUrl: './team-staff-member-detail.component.html',
})
export class TeamStaffMemberDetailComponent implements OnInit {
  teamStaffMember: ITeamStaffMember | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamStaffMember }) => {
      this.teamStaffMember = teamStaffMember;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
