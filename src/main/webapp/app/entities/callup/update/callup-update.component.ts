import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICallup, Callup } from '../callup.model';
import { CallupService } from '../service/callup.service';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { MatchPlayerService } from 'app/entities/match-player/service/match-player.service';
import { ITeamStaffMember } from 'app/entities/team-staff-member/team-staff-member.model';
import { TeamStaffMemberService } from 'app/entities/team-staff-member/service/team-staff-member.service';

@Component({
  selector: 'jhi-callup-update',
  templateUrl: './callup-update.component.html',
})
export class CallupUpdateComponent implements OnInit {
  isSaving = false;

  matchPlayersSharedCollection: IMatchPlayer[] = [];
  captainsCollection: IMatchPlayer[] = [];
  teamStaffMembersSharedCollection: ITeamStaffMember[] = [];

  editForm = this.fb.group({
    id: [],
    captain: [],
    dt: [],
    dt2: [],
    teamDelegate: [],
    players: [],
  });

  constructor(
    protected callupService: CallupService,
    protected matchPlayerService: MatchPlayerService,
    protected teamStaffMemberService: TeamStaffMemberService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ callup }) => {
      this.updateForm(callup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const callup = this.createFromForm();
    if (callup.id !== undefined) {
      this.subscribeToSaveResponse(this.callupService.update(callup));
    } else {
      this.subscribeToSaveResponse(this.callupService.create(callup));
    }
  }

  trackMatchPlayerById(_index: number, item: IMatchPlayer): number {
    return item.id!;
  }

  trackTeamStaffMemberById(_index: number, item: ITeamStaffMember): number {
    return item.id!;
  }

  getSelectedMatchPlayer(option: IMatchPlayer, selectedVals?: IMatchPlayer[]): IMatchPlayer {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICallup>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(callup: ICallup): void {
    this.editForm.patchValue({
      id: callup.id,
      captain: callup.captain,
      dt: callup.dt,
      dt2: callup.dt2,
      teamDelegate: callup.teamDelegate,
      players: callup.players,
    });

    this.matchPlayersSharedCollection = this.matchPlayerService.addMatchPlayerToCollectionIfMissing(
      this.matchPlayersSharedCollection,
      ...(callup.players ?? [])
    );
    this.captainsCollection = this.matchPlayerService.addMatchPlayerToCollectionIfMissing(this.captainsCollection, callup.captain);
    this.teamStaffMembersSharedCollection = this.teamStaffMemberService.addTeamStaffMemberToCollectionIfMissing(
      this.teamStaffMembersSharedCollection,
      callup.dt,
      callup.dt2,
      callup.teamDelegate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.matchPlayerService
      .query()
      .pipe(map((res: HttpResponse<IMatchPlayer[]>) => res.body ?? []))
      .pipe(
        map((matchPlayers: IMatchPlayer[]) =>
          this.matchPlayerService.addMatchPlayerToCollectionIfMissing(matchPlayers, ...(this.editForm.get('players')!.value ?? []))
        )
      )
      .subscribe((matchPlayers: IMatchPlayer[]) => (this.matchPlayersSharedCollection = matchPlayers));

    this.matchPlayerService
      .query({ filter: 'captaincallup-is-null' })
      .pipe(map((res: HttpResponse<IMatchPlayer[]>) => res.body ?? []))
      .pipe(
        map((matchPlayers: IMatchPlayer[]) =>
          this.matchPlayerService.addMatchPlayerToCollectionIfMissing(matchPlayers, this.editForm.get('captain')!.value)
        )
      )
      .subscribe((matchPlayers: IMatchPlayer[]) => (this.captainsCollection = matchPlayers));

    this.teamStaffMemberService
      .query()
      .pipe(map((res: HttpResponse<ITeamStaffMember[]>) => res.body ?? []))
      .pipe(
        map((teamStaffMembers: ITeamStaffMember[]) =>
          this.teamStaffMemberService.addTeamStaffMemberToCollectionIfMissing(
            teamStaffMembers,
            this.editForm.get('dt')!.value,
            this.editForm.get('dt2')!.value,
            this.editForm.get('teamDelegate')!.value
          )
        )
      )
      .subscribe((teamStaffMembers: ITeamStaffMember[]) => (this.teamStaffMembersSharedCollection = teamStaffMembers));
  }

  protected createFromForm(): ICallup {
    return {
      ...new Callup(),
      id: this.editForm.get(['id'])!.value,
      captain: this.editForm.get(['captain'])!.value,
      dt: this.editForm.get(['dt'])!.value,
      dt2: this.editForm.get(['dt2'])!.value,
      teamDelegate: this.editForm.get(['teamDelegate'])!.value,
      players: this.editForm.get(['players'])!.value,
    };
  }
}
