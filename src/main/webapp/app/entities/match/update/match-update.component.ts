import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMatch, Match } from '../match.model';
import { MatchService } from '../service/match.service';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { MatchPlayerService } from 'app/entities/match-player/service/match-player.service';
import { ICallup } from 'app/entities/callup/callup.model';
import { CallupService } from 'app/entities/callup/service/callup.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IStadium } from 'app/entities/stadium/stadium.model';
import { StadiumService } from 'app/entities/stadium/service/stadium.service';
import { ITeamStaffMember } from 'app/entities/team-staff-member/team-staff-member.model';
import { TeamStaffMemberService } from 'app/entities/team-staff-member/service/team-staff-member.service';
import { IShirt } from 'app/entities/shirt/shirt.model';
import { ShirtService } from 'app/entities/shirt/service/shirt.service';
import { IMatchday } from 'app/entities/matchday/matchday.model';
import { MatchdayService } from 'app/entities/matchday/service/matchday.service';
import { IReferee } from 'app/entities/referee/referee.model';
import { RefereeService } from 'app/entities/referee/service/referee.service';

@Component({
  selector: 'jhi-match-update',
  templateUrl: './match-update.component.html',
})
export class MatchUpdateComponent implements OnInit {
  isSaving = false;

  motmsCollection: IMatchPlayer[] = [];
  homeCallupsCollection: ICallup[] = [];
  awayCallupsCollection: ICallup[] = [];
  teamsSharedCollection: ITeam[] = [];
  stadiumsSharedCollection: IStadium[] = [];
  teamStaffMembersSharedCollection: ITeamStaffMember[] = [];
  shirtsSharedCollection: IShirt[] = [];
  matchdaysSharedCollection: IMatchday[] = [];
  refereesSharedCollection: IReferee[] = [];

  editForm = this.fb.group({
    id: [],
    moment: [],
    attendance: [null, [Validators.min(0)]],
    motm: [],
    homeCallup: [],
    awayCallup: [],
    homeTeam: [null, Validators.required],
    awayTeam: [null, Validators.required],
    stadium: [],
    matchDelegate: [],
    homeShirt: [],
    awayShirt: [],
    matchday: [],
    referees: [],
  });

  constructor(
    protected matchService: MatchService,
    protected matchPlayerService: MatchPlayerService,
    protected callupService: CallupService,
    protected teamService: TeamService,
    protected stadiumService: StadiumService,
    protected teamStaffMemberService: TeamStaffMemberService,
    protected shirtService: ShirtService,
    protected matchdayService: MatchdayService,
    protected refereeService: RefereeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ match }) => {
      if (match.id === undefined) {
        const today = dayjs().startOf('day');
        match.moment = today;
      }

      this.updateForm(match);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const match = this.createFromForm();
    if (match.id !== undefined) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  trackMatchPlayerById(_index: number, item: IMatchPlayer): number {
    return item.id!;
  }

  trackCallupById(_index: number, item: ICallup): number {
    return item.id!;
  }

  trackTeamById(_index: number, item: ITeam): number {
    return item.id!;
  }

  trackStadiumById(_index: number, item: IStadium): number {
    return item.id!;
  }

  trackTeamStaffMemberById(_index: number, item: ITeamStaffMember): number {
    return item.id!;
  }

  trackShirtById(_index: number, item: IShirt): number {
    return item.id!;
  }

  trackMatchdayById(_index: number, item: IMatchday): number {
    return item.id!;
  }

  trackRefereeById(_index: number, item: IReferee): number {
    return item.id!;
  }

  getSelectedReferee(option: IReferee, selectedVals?: IReferee[]): IReferee {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>): void {
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

  protected updateForm(match: IMatch): void {
    this.editForm.patchValue({
      id: match.id,
      moment: match.moment ? match.moment.format(DATE_TIME_FORMAT) : null,
      attendance: match.attendance,
      motm: match.motm,
      homeCallup: match.homeCallup,
      awayCallup: match.awayCallup,
      homeTeam: match.homeTeam,
      awayTeam: match.awayTeam,
      stadium: match.stadium,
      matchDelegate: match.matchDelegate,
      homeShirt: match.homeShirt,
      awayShirt: match.awayShirt,
      matchday: match.matchday,
      referees: match.referees,
    });

    this.motmsCollection = this.matchPlayerService.addMatchPlayerToCollectionIfMissing(this.motmsCollection, match.motm);
    this.homeCallupsCollection = this.callupService.addCallupToCollectionIfMissing(this.homeCallupsCollection, match.homeCallup);
    this.awayCallupsCollection = this.callupService.addCallupToCollectionIfMissing(this.awayCallupsCollection, match.awayCallup);
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, match.homeTeam, match.awayTeam);
    this.stadiumsSharedCollection = this.stadiumService.addStadiumToCollectionIfMissing(this.stadiumsSharedCollection, match.stadium);
    this.teamStaffMembersSharedCollection = this.teamStaffMemberService.addTeamStaffMemberToCollectionIfMissing(
      this.teamStaffMembersSharedCollection,
      match.matchDelegate
    );
    this.shirtsSharedCollection = this.shirtService.addShirtToCollectionIfMissing(
      this.shirtsSharedCollection,
      match.homeShirt,
      match.awayShirt
    );
    this.matchdaysSharedCollection = this.matchdayService.addMatchdayToCollectionIfMissing(this.matchdaysSharedCollection, match.matchday);
    this.refereesSharedCollection = this.refereeService.addRefereeToCollectionIfMissing(
      this.refereesSharedCollection,
      ...(match.referees ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.matchPlayerService
      .query({ filter: 'motmmatch-is-null' })
      .pipe(map((res: HttpResponse<IMatchPlayer[]>) => res.body ?? []))
      .pipe(
        map((matchPlayers: IMatchPlayer[]) =>
          this.matchPlayerService.addMatchPlayerToCollectionIfMissing(matchPlayers, this.editForm.get('motm')!.value)
        )
      )
      .subscribe((matchPlayers: IMatchPlayer[]) => (this.motmsCollection = matchPlayers));

    this.callupService
      .query({ filter: 'homematch-is-null' })
      .pipe(map((res: HttpResponse<ICallup[]>) => res.body ?? []))
      .pipe(map((callups: ICallup[]) => this.callupService.addCallupToCollectionIfMissing(callups, this.editForm.get('homeCallup')!.value)))
      .subscribe((callups: ICallup[]) => (this.homeCallupsCollection = callups));

    this.callupService
      .query({ filter: 'awaymatch-is-null' })
      .pipe(map((res: HttpResponse<ICallup[]>) => res.body ?? []))
      .pipe(map((callups: ICallup[]) => this.callupService.addCallupToCollectionIfMissing(callups, this.editForm.get('awayCallup')!.value)))
      .subscribe((callups: ICallup[]) => (this.awayCallupsCollection = callups));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(
        map((teams: ITeam[]) =>
          this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('homeTeam')!.value, this.editForm.get('awayTeam')!.value)
        )
      )
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.stadiumService
      .query()
      .pipe(map((res: HttpResponse<IStadium[]>) => res.body ?? []))
      .pipe(
        map((stadiums: IStadium[]) => this.stadiumService.addStadiumToCollectionIfMissing(stadiums, this.editForm.get('stadium')!.value))
      )
      .subscribe((stadiums: IStadium[]) => (this.stadiumsSharedCollection = stadiums));

    this.teamStaffMemberService
      .query()
      .pipe(map((res: HttpResponse<ITeamStaffMember[]>) => res.body ?? []))
      .pipe(
        map((teamStaffMembers: ITeamStaffMember[]) =>
          this.teamStaffMemberService.addTeamStaffMemberToCollectionIfMissing(teamStaffMembers, this.editForm.get('matchDelegate')!.value)
        )
      )
      .subscribe((teamStaffMembers: ITeamStaffMember[]) => (this.teamStaffMembersSharedCollection = teamStaffMembers));

    this.shirtService
      .query()
      .pipe(map((res: HttpResponse<IShirt[]>) => res.body ?? []))
      .pipe(
        map((shirts: IShirt[]) =>
          this.shirtService.addShirtToCollectionIfMissing(
            shirts,
            this.editForm.get('homeShirt')!.value,
            this.editForm.get('awayShirt')!.value
          )
        )
      )
      .subscribe((shirts: IShirt[]) => (this.shirtsSharedCollection = shirts));

    this.matchdayService
      .query()
      .pipe(map((res: HttpResponse<IMatchday[]>) => res.body ?? []))
      .pipe(
        map((matchdays: IMatchday[]) =>
          this.matchdayService.addMatchdayToCollectionIfMissing(matchdays, this.editForm.get('matchday')!.value)
        )
      )
      .subscribe((matchdays: IMatchday[]) => (this.matchdaysSharedCollection = matchdays));

    this.refereeService
      .query()
      .pipe(map((res: HttpResponse<IReferee[]>) => res.body ?? []))
      .pipe(
        map((referees: IReferee[]) =>
          this.refereeService.addRefereeToCollectionIfMissing(referees, ...(this.editForm.get('referees')!.value ?? []))
        )
      )
      .subscribe((referees: IReferee[]) => (this.refereesSharedCollection = referees));
  }

  protected createFromForm(): IMatch {
    return {
      ...new Match(),
      id: this.editForm.get(['id'])!.value,
      moment: this.editForm.get(['moment'])!.value ? dayjs(this.editForm.get(['moment'])!.value, DATE_TIME_FORMAT) : undefined,
      attendance: this.editForm.get(['attendance'])!.value,
      motm: this.editForm.get(['motm'])!.value,
      homeCallup: this.editForm.get(['homeCallup'])!.value,
      awayCallup: this.editForm.get(['awayCallup'])!.value,
      homeTeam: this.editForm.get(['homeTeam'])!.value,
      awayTeam: this.editForm.get(['awayTeam'])!.value,
      stadium: this.editForm.get(['stadium'])!.value,
      matchDelegate: this.editForm.get(['matchDelegate'])!.value,
      homeShirt: this.editForm.get(['homeShirt'])!.value,
      awayShirt: this.editForm.get(['awayShirt'])!.value,
      matchday: this.editForm.get(['matchday'])!.value,
      referees: this.editForm.get(['referees'])!.value,
    };
  }
}
