import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompetitionPlayer, CompetitionPlayer } from '../competition-player.model';
import { CompetitionPlayerService } from '../service/competition-player.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';

@Component({
  selector: 'jhi-competition-player-update',
  templateUrl: './competition-player-update.component.html',
})
export class CompetitionPlayerUpdateComponent implements OnInit {
  isSaving = false;

  playersSharedCollection: IPlayer[] = [];
  competitionsSharedCollection: ICompetition[] = [];
  positionsSharedCollection: IPosition[] = [];

  editForm = this.fb.group({
    id: [],
    preferredShirtNumber: [null, [Validators.min(0)]],
    player: [null, Validators.required],
    competition: [null, Validators.required],
    preferredPositions: [],
  });

  constructor(
    protected competitionPlayerService: CompetitionPlayerService,
    protected playerService: PlayerService,
    protected competitionService: CompetitionService,
    protected positionService: PositionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competitionPlayer }) => {
      this.updateForm(competitionPlayer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competitionPlayer = this.createFromForm();
    if (competitionPlayer.id !== undefined) {
      this.subscribeToSaveResponse(this.competitionPlayerService.update(competitionPlayer));
    } else {
      this.subscribeToSaveResponse(this.competitionPlayerService.create(competitionPlayer));
    }
  }

  trackPlayerById(_index: number, item: IPlayer): number {
    return item.id!;
  }

  trackCompetitionById(_index: number, item: ICompetition): number {
    return item.id!;
  }

  trackPositionById(_index: number, item: IPosition): number {
    return item.id!;
  }

  getSelectedPosition(option: IPosition, selectedVals?: IPosition[]): IPosition {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetitionPlayer>>): void {
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

  protected updateForm(competitionPlayer: ICompetitionPlayer): void {
    this.editForm.patchValue({
      id: competitionPlayer.id,
      preferredShirtNumber: competitionPlayer.preferredShirtNumber,
      player: competitionPlayer.player,
      competition: competitionPlayer.competition,
      preferredPositions: competitionPlayer.preferredPositions,
    });

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing(
      this.playersSharedCollection,
      competitionPlayer.player
    );
    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing(
      this.competitionsSharedCollection,
      competitionPlayer.competition
    );
    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing(
      this.positionsSharedCollection,
      ...(competitionPlayer.preferredPositions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing(players, this.editForm.get('player')!.value)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.competitionService
      .query()
      .pipe(map((res: HttpResponse<ICompetition[]>) => res.body ?? []))
      .pipe(
        map((competitions: ICompetition[]) =>
          this.competitionService.addCompetitionToCollectionIfMissing(competitions, this.editForm.get('competition')!.value)
        )
      )
      .subscribe((competitions: ICompetition[]) => (this.competitionsSharedCollection = competitions));

    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing(positions, ...(this.editForm.get('preferredPositions')!.value ?? []))
        )
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));
  }

  protected createFromForm(): ICompetitionPlayer {
    return {
      ...new CompetitionPlayer(),
      id: this.editForm.get(['id'])!.value,
      preferredShirtNumber: this.editForm.get(['preferredShirtNumber'])!.value,
      player: this.editForm.get(['player'])!.value,
      competition: this.editForm.get(['competition'])!.value,
      preferredPositions: this.editForm.get(['preferredPositions'])!.value,
    };
  }
}
