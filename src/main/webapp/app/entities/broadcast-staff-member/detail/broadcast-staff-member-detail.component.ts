import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBroadcastStaffMember } from '../broadcast-staff-member.model';

@Component({
  selector: 'jhi-broadcast-staff-member-detail',
  templateUrl: './broadcast-staff-member-detail.component.html',
})
export class BroadcastStaffMemberDetailComponent implements OnInit {
  broadcastStaffMember: IBroadcastStaffMember | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ broadcastStaffMember }) => {
      this.broadcastStaffMember = broadcastStaffMember;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
